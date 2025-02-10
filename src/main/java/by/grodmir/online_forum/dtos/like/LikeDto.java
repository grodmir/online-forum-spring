package by.grodmir.online_forum.dtos.like;

import by.grodmir.online_forum.entities.EntityType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeDto {
    private Integer entityId;
    private EntityType entityType;
    private Boolean isLike;
}
