package by.grodmir.online_forum.controllers;

import by.grodmir.online_forum.dtos.JwtRequest;
import by.grodmir.online_forum.dtos.RegisterUserDto;
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
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return authenticationService.createAuthToken(authRequest);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody RegisterUserDto registrationUserDto) {
        return authenticationService.createNewUser(registrationUserDto);
    }
}
