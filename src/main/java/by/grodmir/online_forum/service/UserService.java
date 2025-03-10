package by.grodmir.online_forum.service;

import by.grodmir.online_forum.dto.user.RegisterUserDto;
import by.grodmir.online_forum.entity.User;
import by.grodmir.online_forum.mapper.UserMapper;
import by.grodmir.online_forum.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User not found: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });

        return userMapper.toUserDetails(user);
    }

    @Transactional
    public User createNewUser(RegisterUserDto registrationUserDto) {
        User user = userMapper.toEntity(registrationUserDto);
        return userRepository.save(user);
    }
}
