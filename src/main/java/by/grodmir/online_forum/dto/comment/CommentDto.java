package by.grodmir.online_forum.dto.comment;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Integer id;
    private String username;
    private String content;
    private LocalDateTime createdAt;
}
