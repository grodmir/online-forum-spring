package by.grodmir.online_forum.service;

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
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final CommentRepository commentRepository;

    public void toggleLike(Integer entityId, EntityType entityType, boolean isLike) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        if (entityType == EntityType.TOPIC && !topicRepository.existsById(entityId)) {
            throw new EntityNotFoundException("Топик с id " + entityId + " не найден");
        } else if (entityType == EntityType.COMMENT && !commentRepository.existsById(entityId)) {
            throw new EntityNotFoundException("Комментарий с id " + entityId + " не найден");
        }

        Optional<Like> existingLike = likeRepository.findByUserAndEntityIdAndEntityType(user, entityId, entityType);

        if (existingLike.isPresent()) {
            Like like = existingLike.get();
            if (like.isLiked() == isLike) {
                likeRepository.delete(like);
            } else {
                like.setLiked(isLike);
                likeRepository.save(like);
            }
        } else {
            Like newLike = new Like();
            newLike.setUser(user);
            newLike.setEntityId(entityId);
            newLike.setEntityType(entityType);
            newLike.setLiked(isLike);
            likeRepository.save(newLike);
        }
    }

    public int countLikes(Integer entityId,EntityType entityType) {
        return likeRepository.countByEntityIdAndEntityTypeAndLiked(entityId, entityType, true);
    }

    public int countDislikes(Integer entityId,EntityType entityType) {
        return likeRepository.countByEntityIdAndEntityTypeAndLiked(entityId, entityType, false);
    }
}
