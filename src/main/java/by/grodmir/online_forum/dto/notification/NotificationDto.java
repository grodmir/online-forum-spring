package by.grodmir.online_forum.dto.notification;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {
    private Integer id;
    private String receiverUsername;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;
}
