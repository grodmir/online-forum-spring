package by.grodmir.online_forum.dto.topic;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopicDto {
    private int id;
    private String title;
    private String content;
    private String author;
    private String dateOfCreation;
}
