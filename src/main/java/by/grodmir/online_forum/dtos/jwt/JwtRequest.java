package by.grodmir.online_forum.dtos.jwt;

import lombok.Data;

@Data
public class JwtRequest {
    private String username;
    private String password;
}
