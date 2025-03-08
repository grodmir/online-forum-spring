package by.grodmir.online_forum.service;

import by.grodmir.online_forum.dto.topic.CreateAndUpdateTopicDto;
import by.grodmir.online_forum.dto.topic.TopicDto;
import by.grodmir.online_forum.entity.Topic;
import by.grodmir.online_forum.entity.User;
import by.grodmir.online_forum.exception.UserNotFoundException;
import by.grodmir.online_forum.mapper.TopicMapper;
import by.grodmir.online_forum.repository.TopicRepository;
import by.grodmir.online_forum.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TopicService {
    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;
    private final SecurityService securityService;

    @Transactional(readOnly = true)
    public List<TopicDto> getAllTopics() {
        return topicRepository.findAll().stream()
                .map(topicMapper::toDto)
                .toList();
    }

    @Transactional
    public TopicDto createTopic(CreateAndUpdateTopicDto createTopicDto) {
        User user = securityService.getCurrentUser();
        Topic topic = buildTopic(createTopicDto, user);
        topicRepository.save(topic);
        return topicMapper.toDto(topic);
    }

    @Transactional(readOnly = true)
    public TopicDto getTopicById(Integer id) {
        return topicMapper.toDto(findTopicById(id));
    }

    @Transactional
    public TopicDto updateTopic(Integer id, CreateAndUpdateTopicDto updateTopicDto) {
        Topic topic = findTopicById(id);
        checkTopicOwnership(topic);
        updateTopicFields(topic, updateTopicDto);
        return topicMapper.toDto(topic);
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
        String currentUsername = securityService.getCurrentUser().getUsername();
        if (!topic.getUser().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("You don't have permission for this action");
        }
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
}
