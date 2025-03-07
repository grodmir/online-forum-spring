package by.grodmir.online_forum.service;

import by.grodmir.online_forum.dto.topic.CreateAndUpdateTopicDto;
import by.grodmir.online_forum.dto.topic.TopicDto;
import by.grodmir.online_forum.entity.Topic;
import by.grodmir.online_forum.entity.User;
import by.grodmir.online_forum.exception.UserNotFoundException;
import by.grodmir.online_forum.repository.TopicRepository;
import by.grodmir.online_forum.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Все ответы сервис предоставляет в виде DTO класса
 * */
@Service
@Slf4j
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    public List<TopicDto> getAllTopics() {
        return topicRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional
    public TopicDto createTopic(CreateAndUpdateTopicDto createTopicDto) {
        User user = getCurrentUser();
        Topic topic = buildTopic(createTopicDto, user);
        topicRepository.save(topic);
        return mapToDto(topic);
    }

    public TopicDto getTopicById(Integer id) {
        return mapToDto(findTopicById(id));
    }

    @Transactional
    public TopicDto updateTopic(Integer id, CreateAndUpdateTopicDto updateTopicDto) {
        Topic topic = findTopicById(id);
        checkTopicOwnership(topic);
        updateTopicFields(topic, updateTopicDto);
        return mapToDto(topic);
    }

    @Transactional
    public void deleteTopic(Integer id) {
        Topic topic = findTopicById(id);
        checkTopicOwnership(topic);
        topicRepository.delete(topic);
    }

    private Topic findTopicById(Integer id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found with id: " + id));
    }

    private void checkTopicOwnership(Topic topic) {
        String currentUsername = getCurrentUser().getUsername();
        if (!topic.getUser().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("You don't have permission for this action");
        }
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    private Topic buildTopic(CreateAndUpdateTopicDto dto, User user) {
        return Topic.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .build();
    }

    private void updateTopicFields(Topic topic, CreateAndUpdateTopicDto dto) {
        topic.setTitle(dto.getTitle());
        topic.setContent(dto.getContent());
    }

    /**
     * Метод преобразующий Entity класс в DTO класс
     * */
    private TopicDto mapToDto(Topic topic) {
        return new TopicDto(
                topic.getId(),
                topic.getTitle(),
                topic.getContent(),
                topic.getUser().getUsername(),
                topic.getCreated_at().toString()
        );
    }
}
