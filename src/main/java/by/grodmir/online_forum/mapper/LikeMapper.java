package by.grodmir.online_forum.mapper;

import by.grodmir.online_forum.dto.like.LikeDto;
import by.grodmir.online_forum.entity.EntityType;
import by.grodmir.online_forum.entity.Like;
import by.grodmir.online_forum.entity.User;
import org.springframework.stereotype.Component;

@Component
public class LikeMapper {
    public LikeDto toDto(Like like, Boolean isLiked) {
        return LikeDto.builder()
                .entityId(like.getEntityId())
                .entityType(like.getEntityType())
                .isLike(isLiked != null ? isLiked : like.isLiked())
                .build();
    }

    public LikeDto toRemovedDto(Like like) {
        return LikeDto.builder()
                .entityId(like.getEntityId())
                .entityType(like.getEntityType())
                .isLike(null)
                .build();
    }

    public Like toEntity(User user, Integer entityId, EntityType entityType, boolean isLike) {
        return Like.builder()
                .user(user)
                .entityId(entityId)
                .entityType(entityType)
                .liked(isLike)
                .build();
    }
}
