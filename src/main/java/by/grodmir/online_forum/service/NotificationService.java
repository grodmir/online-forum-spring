package by.grodmir.online_forum.service;

import by.grodmir.online_forum.dto.notification.NotificationDto;
import by.grodmir.online_forum.entity.Notification;
import by.grodmir.online_forum.entity.User;
import by.grodmir.online_forum.mapper.NotificationMapper;
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
    private final NotificationMapper notificationMapper;

    public void sendNotification(String username, String message) {
        User receiver = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + username));
        notificationRepository.save(notificationMapper.toEntity(message, receiver));
    }

    public List<NotificationDto> getUserNotifications(String username) {
        return notificationRepository.findByReceiverUsernameOrderByCreatedAtDesc(username)
                .stream()
                .map(notificationMapper::toDto)
                .toList();
    }

    @Transactional
    public void markAsRead(Integer notificationId, String username) {
        Notification notification = findNotificationById(notificationId);
        checkNotificationOwnership(notification, username);
        notification.setIsRead(true);
        notificationRepository.save(notification);
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
