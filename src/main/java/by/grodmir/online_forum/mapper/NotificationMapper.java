package by.grodmir.online_forum.mapper;

import by.grodmir.online_forum.dto.notification.NotificationDto;
import by.grodmir.online_forum.entity.Notification;
import by.grodmir.online_forum.entity.User;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationDto toDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .receiverUsername(notification.getReceiver().getUsername())
                .message(notification.getMessage())
                .read(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    public Notification toEntity(String message, User receiver) {
        return Notification.builder()
                .receiver(receiver)
                .message(message)
                .isRead(false)
                .build();
    }
}
