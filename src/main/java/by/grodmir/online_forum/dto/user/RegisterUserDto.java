package by.grodmir.online_forum.dto.user;

import lombok.Data;

@Data
public class RegisterUserDto {
    private String username;
    private String password;
    private String confirmPassword;
    private String email;
}
