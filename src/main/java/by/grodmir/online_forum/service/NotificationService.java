package by.grodmir.online_forum.service;

import by.grodmir.online_forum.dto.notification.NotificationDto;
import by.grodmir.online_forum.entity.Notification;
import by.grodmir.online_forum.entity.User;
import by.grodmir.online_forum.repository.NotificationRepository;
import by.grodmir.online_forum.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public void sendNotification(String username, String message) {
        User receiver = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
        notificationRepository.save(buildNotification(receiver, message));
    }

    public List<NotificationDto> getUserNotifications(String username) {
        return notificationRepository.findByReceiverUsernameOrderByCreatedAtDesc(username)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional
    public void markAsRead(Integer notificationId, String username) {
        Notification notification = findNotificationById(notificationId);
        checkNotificationOwnership(notification, username);
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

    private Notification buildNotification(User receiver, String message) {
        return Notification.builder()
                .receiver(receiver)
                .message(message)
                .build();
    }

    private Notification findNotificationById(Integer notificationId) {
        return notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
    }

    private void checkNotificationOwnership(Notification notification, String username) {
        if (!notification.getReceiver().getUsername().equals(username)) {
            throw new AccessDeniedException("You cannot modify other users' notifications");
        }
    }
}
