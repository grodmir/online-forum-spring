package by.grodmir.online_forum.dto.topic;

import lombok.Data;

@Data
public class CreateAndUpdateTopicDto {
    private String title;
    private String content;
}
