package by.grodmir.online_forum.service;

import by.grodmir.online_forum.dtos.CreateTopicDto;
import by.grodmir.online_forum.dtos.TopicDto;
import by.grodmir.online_forum.entities.Topic;
import by.grodmir.online_forum.entities.User;
import by.grodmir.online_forum.repositories.TopicRepository;
import by.grodmir.online_forum.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        log.info("Get all topics");
        List<Topic> topics = topicRepository.findAll();
        return topics.stream()
                .map(this::matToDto)
                .toList();
    }

    public TopicDto createTopic(CreateTopicDto createTopicDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Topic topic = new Topic();
        topic.setTitle(createTopicDto.getTitle());
        topic.setContent(createTopicDto.getContent());
        topic.setUser(user);
        topicRepository.save(topic);

        return new TopicDto(topic.getId(), topic.getTitle(),
                topic.getContent(), topic.getUser().getUsername(),
                topic.getCreated_at().toString());
    }

    private TopicDto matToDto(Topic topic) {
        return new TopicDto(
                topic.getId(),
                topic.getTitle(),
                topic.getContent(),
                topic.getUser().getUsername(),
                topic.getCreated_at().toString()
        );
    }
}
