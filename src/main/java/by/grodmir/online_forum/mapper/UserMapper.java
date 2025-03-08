package by.grodmir.online_forum.mapper;

import by.grodmir.online_forum.dto.user.RegisterUserDto;
import by.grodmir.online_forum.entity.Role;
import by.grodmir.online_forum.entity.User;
import by.grodmir.online_forum.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public User toEntity(RegisterUserDto registerUserDto) {
        return User.builder()
                .username(registerUserDto.getUsername())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .email(registerUserDto.getEmail())
                .roles(List.of(roleService.getRoleUser()))
                .build();
    }

    public UserDetails toUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getRoles().stream()
                        .map(this::toGrantedAuthority)
                        .toList()
        );
    }

    private SimpleGrantedAuthority toGrantedAuthority(Role role) {
        return new SimpleGrantedAuthority(role.getName());
    }
}
