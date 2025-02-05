package by.grodmir.online_forum.dtos;

import lombok.Data;

@Data
public class CreateTopicDto {
    private String title;
    private String content;
}
