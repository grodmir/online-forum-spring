package by.grodmir.online_forum.dto.like;

import by.grodmir.online_forum.entity.EntityType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeDto {
    private Integer entityId;
    private EntityType entityType;
    private Boolean isLike;
}
