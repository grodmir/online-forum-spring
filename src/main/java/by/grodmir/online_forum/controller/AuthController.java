package by.grodmir.online_forum.controller;

import by.grodmir.online_forum.dto.jwt.JwtRequest;
import by.grodmir.online_forum.dto.jwt.JwtResponse;
import by.grodmir.online_forum.dto.user.RegisterUserDto;
import by.grodmir.online_forum.dto.user.UserDto;
import by.grodmir.online_forum.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/auth")
    public ResponseEntity<JwtResponse> createAuthToken(@RequestBody JwtRequest authRequest) {
        return ResponseEntity.ok(authenticationService.createAuthToken(authRequest));
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDto> createNewUser(@RequestBody RegisterUserDto registrationUserDto) {
        return ResponseEntity.ok(authenticationService.createNewUser(registrationUserDto));
    }
}
