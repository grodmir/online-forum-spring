package by.grodmir.online_forum.validator;

import by.grodmir.online_forum.service.SecurityService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeValidator {
    private final SecurityService securityService;

    public void validateLikeOwnership(String entityOwnerUsername) {
        String currentUser = securityService.getCurrentUser().getUsername();
        if (currentUser.equals(entityOwnerUsername)) {
            throw new ValidationException("You cannot like/dislike your own content");
        }
    }
}
