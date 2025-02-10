package by.grodmir.online_forum.service;

import by.grodmir.online_forum.dtos.notification.NotificationDto;
import by.grodmir.online_forum.entities.Notification;
import by.grodmir.online_forum.repositories.NotificationRepository;
import by.grodmir.online_forum.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public void sendNotification(String username, String message) {
        userRepository.findByUsername(username).ifPresent(user -> {
            Notification notification = new Notification();
            notification.setReceiver(user);
            notification.setMessage(message);
            notificationRepository.save(notification);
        });
    }

    public List<NotificationDto> getUserNotifications(String username) {
        List<Notification> notifications = notificationRepository.findByReceiverUsernameOrderByCreatedAtDesc(username);
        return notifications.stream().map(this::mapToDto).toList();
    }

    public void markAsRead(Integer notificationId, String username) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Уведомление не найдено"));

        if (!notification.getReceiver().getUsername().equals(username)) {
            throw new AccessDeniedException("Вы не можете изменять чужие уведомления");
        }

        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    public NotificationDto mapToDto(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getReceiver().getUsername(),
                notification.getMessage(),
                notification.getIsRead(),
                notification.getCreatedAt()
        );
    }
}
