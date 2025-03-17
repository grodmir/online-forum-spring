package by.grodmir.online_forum.validator;

import by.grodmir.online_forum.entity.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationValidator {

    public void validateNotificationOwnership(Notification notification, String username) {
        if (!notification.getReceiver().getUsername().equals(username)) {
            throw new AccessDeniedException("You cannot modify other users' notifications");
        }
    }
}
