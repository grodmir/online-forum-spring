package by.grodmir.online_forum.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateAndUpdateCommentDto {
    @NotBlank(message = "Content cannot be empty")
    private String content;
}
