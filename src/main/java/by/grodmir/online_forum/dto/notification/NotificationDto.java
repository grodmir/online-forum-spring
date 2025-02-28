package by.grodmir.online_forum.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificationDto {
    private Integer id;
    private String receiverUsername;
    private String message;
    private boolean read;
    private LocalDateTime createdAt;
}
