package by.grodmir.online_forum.mapper;

import by.grodmir.online_forum.dto.like.LikeDto;
import by.grodmir.online_forum.entity.EntityType;
import by.grodmir.online_forum.entity.Like;
import by.grodmir.online_forum.entity.User;
import org.springframework.stereotype.Component;

@Component
public class LikeMapper {
    public LikeDto toDto(Like like, Boolean isLiked) {
        return new LikeDto(
                like.getEntityId(),
                like.getEntityType(),
                isLiked != null ? isLiked : like.isLiked()
        );
    }

    public LikeDto toRemovedDto(Like like) {
        return new LikeDto(
                like.getEntityId(),
                like.getEntityType(),
                null
        );
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
