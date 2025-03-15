package by.grodmir.online_forum.dto.topic;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TopicDto {
    private int id;
    private String title;
    private String content;
    private String author;
    private String createdAt;
}
