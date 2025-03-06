package by.grodmir.online_forum.service;

import by.grodmir.online_forum.dto.topic.CreateAndUpdateTopicDto;
import by.grodmir.online_forum.dto.topic.TopicDto;
import by.grodmir.online_forum.entity.Topic;
import by.grodmir.online_forum.entity.User;
import by.grodmir.online_forum.exception.UserNotFoundException;
import by.grodmir.online_forum.repository.TopicRepository;
import by.grodmir.online_forum.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TopicService {
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository, UserRepository userRepository) {
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    public List<TopicDto> getAllTopics() {
        List<Topic> topics = topicRepository.findAll();
        return topics.stream()
                .map(this::mapToDto)
                .toList();
    }

    public TopicDto createTopic(CreateAndUpdateTopicDto createTopicDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Topic topic = new Topic();
        topic.setTitle(createTopicDto.getTitle());
        topic.setContent(createTopicDto.getContent());
        topic.setUser(user);
        topicRepository.save(topic);

        return mapToDto(topic);
    }

    public TopicDto getTopicById(Integer id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found with id: " + id));

        return mapToDto(topic);
    }

    public TopicDto updateTopic(Integer id, CreateAndUpdateTopicDto updateTopicDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found with id: " + id));

        if (!topic.getUser().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("You cannot edit this topic");
        }

        topic.setTitle(updateTopicDto.getTitle());
        topic.setContent(updateTopicDto.getContent());
        topicRepository.save(topic);

        return mapToDto(topic);
    }

    public void deleteTopic(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found with id: " + id));

        if (!topic.getUser().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("You can't delete this topic");
        }

        topicRepository.delete(topic);
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
