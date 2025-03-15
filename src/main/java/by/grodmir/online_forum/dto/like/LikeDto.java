package by.grodmir.online_forum.dto.like;

import by.grodmir.online_forum.entity.EntityType;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikeDto {
    private Integer entityId;
    private EntityType entityType;
    private Boolean isLike;
}
