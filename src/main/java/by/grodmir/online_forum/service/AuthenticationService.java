package by.grodmir.online_forum.service;

import by.grodmir.online_forum.dto.jwt.JwtRequest;
import by.grodmir.online_forum.dto.jwt.JwtResponse;
import by.grodmir.online_forum.dto.user.RegisterUserDto;
import by.grodmir.online_forum.dto.user.UserDto;
import by.grodmir.online_forum.entity.User;
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
    private final JwtTokenService jwtTokenService;

    public JwtResponse createAuthToken(@RequestBody JwtRequest authRequest) {
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

    public UserDto createNewUser(@RequestBody RegisterUserDto registerUserDto) {
        validatePasswordsMatch(registerUserDto.getPassword(), registerUserDto.getConfirmPassword());
        validateUsernameIsUnique(registerUserDto.getUsername());
        User user = userService.createNewUser(registerUserDto);
        return new UserDto(user.getId(), user.getUsername(), user.getEmail());
    }

    private void validatePasswordsMatch(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("The passwords do not match");
        }
    }

    private void validateUsernameIsUnique(String username) {
        if (userService.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("A user with the specified name already exists");
        }
    }
}
