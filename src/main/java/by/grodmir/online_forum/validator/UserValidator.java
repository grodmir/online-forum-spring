package by.grodmir.online_forum.validator;

import by.grodmir.online_forum.dto.user.RegisterUserDto;
import by.grodmir.online_forum.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserService userService;

    public void validateRegistration(RegisterUserDto dto) {
        validateUsernameIsUnique(dto.getUsername());
    }

    public void validateUsernameIsUnique(String username) {
        if (userService.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("A user with the specified name already exists");
        }
    }
}
