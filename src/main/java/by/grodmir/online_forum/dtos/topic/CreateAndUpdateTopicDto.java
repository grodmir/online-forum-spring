package by.grodmir.online_forum.dtos.topic;

import lombok.Data;

@Data
public class CreateAndUpdateTopicDto {
    private String title;
    private String content;
}
