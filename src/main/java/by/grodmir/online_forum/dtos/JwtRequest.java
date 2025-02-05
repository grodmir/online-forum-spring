package by.grodmir.online_forum.dtos;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
