package by.grodmir.online_forum.controller;

import by.grodmir.online_forum.dto.notification.NotificationDto;
import by.grodmir.online_forum.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public List<NotificationDto> getUserNotifications(Authentication authentication) {
        return notificationService.getUserNotifications(authentication.getName());
    }

    @PostMapping("/{id}/read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void readNotification(@PathVariable Integer id, Authentication authentication) {
        notificationService.markAsRead(id, authentication.getName());
    }
}
