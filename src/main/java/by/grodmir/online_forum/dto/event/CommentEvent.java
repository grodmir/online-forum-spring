package by.grodmir.online_forum.dto.event;

import by.grodmir.online_forum.dto.comment.CommentDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEvent {
    private String eventType;
    private CommentDto payload;
}
