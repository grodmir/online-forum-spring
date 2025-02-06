package by.grodmir.online_forum.service;

import by.grodmir.online_forum.dtos.topic.CreateTopicDto;
import by.grodmir.online_forum.dtos.topic.TopicDto;
import by.grodmir.online_forum.entities.Topic;
import by.grodmir.online_forum.entities.User;
import by.grodmir.online_forum.exception.UserNotFoundException;
import by.grodmir.online_forum.repositories.TopicRepository;
import by.grodmir.online_forum.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    public TopicDto createTopic(CreateTopicDto createTopicDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        Topic topic = new Topic();
        topic.setTitle(createTopicDto.getTitle());
        topic.setContent(createTopicDto.getContent());
        topic.setUser(user);
        topicRepository.save(topic);

        return mapToDto(topic);
    }

    public TopicDto getTopicById(Integer id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Топик не найден с id: " + id));

        return mapToDto(topic);
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
