package by.grodmir.online_forum.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentDto {
    private Integer id;
    private String username;
    private String content;
    private LocalDateTime createdAt;
}
