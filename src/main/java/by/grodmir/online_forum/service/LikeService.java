package by.grodmir.online_forum.service;

import by.grodmir.online_forum.dto.like.LikeDto;
import by.grodmir.online_forum.entity.EntityType;
import by.grodmir.online_forum.entity.Like;
import by.grodmir.online_forum.entity.User;
import by.grodmir.online_forum.mapper.LikeMapper;
import by.grodmir.online_forum.repository.CommentRepository;
import by.grodmir.online_forum.repository.LikeRepository;
import by.grodmir.online_forum.repository.TopicRepository;
import by.grodmir.online_forum.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final TopicRepository topicRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final NotificationService notificationService;
    private final SecurityService securityService;
    private final LikeMapper likeMapper;

    public LikeDto toggleLike(Integer entityId, EntityType entityType, boolean isLike) {
        User user =  securityService.getCurrentUser();

        validateEntityExistence(entityId, entityType);

        Optional<Like> existingLike = likeRepository.findByUserAndEntityIdAndEntityType(user, entityId, entityType);
        LikeDto likeDto;
        likeDto = existingLike.map(like -> updateOrRemoveLike(like, isLike))
                .orElseGet(() -> createNewLike(user, entityId, entityType, isLike));

        sendLikeNotification(likeDto);
        return likeDto;
    }

    public int countLikes(Integer entityId, EntityType entityType) {
        return likeRepository.countByEntityIdAndEntityTypeAndLiked(entityId, entityType, true);
    }

    public int countDislikes(Integer entityId, EntityType entityType) {
        return likeRepository.countByEntityIdAndEntityTypeAndLiked(entityId, entityType, false);
    }

    private void validateEntityExistence(Integer entityId, EntityType entityType) {
        boolean exists = switch (entityType) {
            case TOPIC -> topicRepository.existsById(entityId);
            case COMMENT -> commentRepository.existsById(entityId);
        };

        if (!exists) {
            throw new EntityNotFoundException(entityType + " with id " + entityId + " not found");
        }
    }

    private LikeDto updateOrRemoveLike(Like like, boolean isLike) {
        if (like.isLiked() == isLike) {
            likeRepository.delete(like);
            return likeMapper.toRemovedDto(like);
        } else {
            like.setLiked(isLike);
            likeRepository.save(like);
            return likeMapper.toDto(like, isLike);
        }
    }

    private LikeDto createNewLike(User user, Integer entityId, EntityType entityType, boolean isLike) {
        Like newLike = likeMapper.toEntity(user, entityId, entityType, isLike);
        likeRepository.save(newLike);
        return likeMapper.toDto(newLike, isLike);
    }

    private void sendLikeNotification(LikeDto likeDto) {
        if (likeDto.getIsLike() == null) return;

        String entityOwner = findEntityOwner(likeDto.getEntityId(), likeDto.getEntityType());
        if (!entityOwner.equals(securityService.getCurrentUser().getUsername())) {
            String message = likeDto.getIsLike() ? "👍 You got a like!" : "👎 You got a dislike!";
            notificationService.sendNotification(entityOwner, message);
        }
    }

    private String findEntityOwner(Integer entityId, EntityType entityType) {
        return switch (entityType) {
            case TOPIC -> topicRepository.findById(entityId)
                    .orElseThrow(() -> new EntityNotFoundException("Topic not found"))
                    .getUser().getUsername();
            case COMMENT -> commentRepository.findById(entityId)
                    .orElseThrow(() -> new EntityNotFoundException("Comment not found"))
                    .getAuthor().getUsername();
        };
    }
}
