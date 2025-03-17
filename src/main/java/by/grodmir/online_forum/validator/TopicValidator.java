package by.grodmir.online_forum.validator;

import by.grodmir.online_forum.entity.Topic;
import by.grodmir.online_forum.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicValidator {
    private final SecurityService securityService;

    public void validateTopicOwner(Topic topic) {
        String currentUsername = securityService.getCurrentUser().getUsername();
        if (!topic.getUser().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("You do not have permission to access this topic");
        }
    }
}
