package by.grodmir.online_forum.service;

import by.grodmir.online_forum.dto.jwt.JwtRequest;
import by.grodmir.online_forum.dto.jwt.JwtResponse;
import by.grodmir.online_forum.dto.user.RegisterUserDto;
import by.grodmir.online_forum.dto.user.UserDto;
import by.grodmir.online_forum.entity.User;
import by.grodmir.online_forum.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;

    public JwtResponse createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Неправильный логин или пароль");
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        return new JwtResponse(token);
    }

    public UserDto createNewUser(@RequestBody RegisterUserDto registerUserDto) {
        if (!registerUserDto.getPassword().equals(registerUserDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Пароли не совпадают");
        }
        if (userService.findByUsername(registerUserDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с указанным именем уже существует");
        }
        User user = userService.createNewUser(registerUserDto);
        return new UserDto(user.getId(), user.getUsername(), user.getEmail());
    }
}
