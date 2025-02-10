package by.grodmir.online_forum.service;

import by.grodmir.online_forum.dtos.like.LikeDto;
import by.grodmir.online_forum.entities.EntityType;
import by.grodmir.online_forum.entities.Like;
import by.grodmir.online_forum.entities.User;
import by.grodmir.online_forum.exception.UserNotFoundException;
import by.grodmir.online_forum.repositories.CommentRepository;
import by.grodmir.online_forum.repositories.LikeRepository;
import by.grodmir.online_forum.repositories.TopicRepository;
import by.grodmir.online_forum.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final NotificationService notificationService;

    public LikeDto toggleLike(Integer entityId, EntityType entityType, boolean isLike) {
        User user =  getAuthenticatedUser();

        validateEntityExistence(entityId, entityType);

        Optional<Like> existingLike = likeRepository.findByUserAndEntityIdAndEntityType(user, entityId, entityType);
        LikeDto likeDto;
        if (existingLike.isPresent()) {
            likeDto = updateOrRemoveLike(existingLike.get(), isLike);
        } else {
            likeDto = createNewLike(user, entityId, entityType, isLike);
        }

        sendLikeNotification(likeDto);
        return likeDto;
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    private void validateEntityExistence(Integer entityId, EntityType entityType) {
        boolean exists = switch (entityType) {
            case TOPIC -> topicRepository.existsById(entityId);
            case COMMENT -> commentRepository.existsById(entityId);
        };

        if (!exists) {
            throw new EntityNotFoundException(entityType + " с id " + entityId + " не найден");
        }
    }

    private LikeDto updateOrRemoveLike(Like like, boolean isLike) {
        if (like.isLiked() == isLike) {
            likeRepository.delete(like);
            return new LikeDto(like.getEntityId(), like.getEntityType(), null);
        } else {
            like.setLiked(isLike);
            likeRepository.save(like);
            return new LikeDto(like.getEntityId(), like.getEntityType(), isLike);
        }
    }

    private LikeDto createNewLike(User user, Integer entityId, EntityType entityType, boolean isLike) {
        Like newLike = new Like();
        newLike.setUser(user);
        newLike.setEntityId(entityId);
        newLike.setEntityType(entityType);
        newLike.setLiked(isLike);
        likeRepository.save(newLike);
        return new LikeDto(entityId, entityType, isLike);
    }

    private void sendLikeNotification(LikeDto likeDto) {
        if (likeDto.getIsLike() == null) return;

        String entityOwner = findEntityOwner(likeDto.getEntityId(), likeDto.getEntityType());
        if (!entityOwner.equals(getAuthenticatedUser().getUsername())) {
            String message = likeDto.getIsLike() ? "👍 Вам поставили лайк!" : "👎 Вам поставили дизлайк!";
            notificationService.sendNotification(entityOwner, message);
        }
    }

    private String findEntityOwner(Integer entityId, EntityType entityType) {
        return switch (entityType) {
            case TOPIC -> topicRepository.findById(entityId)
                    .orElseThrow(() -> new EntityNotFoundException("Топик не найден"))
                    .getUser().getUsername();
            case COMMENT -> commentRepository.findById(entityId)
                    .orElseThrow(() -> new EntityNotFoundException("Комментарий не найден"))
                    .getUser().getUsername();
        };
    }

    public int countLikes(Integer entityId, EntityType entityType) {
        return (int) likeRepository.countByEntityIdAndEntityTypeAndLiked(entityId, entityType, true);
    }

    public int countDislikes(Integer entityId, EntityType entityType) {
        return (int) likeRepository.countByEntityIdAndEntityTypeAndLiked(entityId, entityType, false);
    }
}
