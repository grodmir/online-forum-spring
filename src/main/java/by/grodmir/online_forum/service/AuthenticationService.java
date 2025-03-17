package by.grodmir.online_forum.service;

import by.grodmir.online_forum.dto.jwt.JwtRequest;
import by.grodmir.online_forum.dto.jwt.JwtResponse;
import by.grodmir.online_forum.dto.user.RegisterUserDto;
import by.grodmir.online_forum.dto.user.UserDto;
import by.grodmir.online_forum.entity.User;
import by.grodmir.online_forum.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserValidator userValidator;

    public JwtResponse createAuthToken(JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect login or password");
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenService.generateToken(userDetails);
        return new JwtResponse(token);
    }

    public UserDto createNewUser(RegisterUserDto registerUserDto) {
        userValidator.validateRegistration(registerUserDto);
        User user = userService.createNewUser(registerUserDto);
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
