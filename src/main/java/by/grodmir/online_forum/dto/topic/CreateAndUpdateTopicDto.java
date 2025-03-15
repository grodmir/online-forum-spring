package by.grodmir.online_forum.dto.topic;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAndUpdateTopicDto {
    @NotBlank(message = "Title cannot be empty")
    private String title;
    @NotBlank(message = "Content cannot be empty")
    private String content;
}
