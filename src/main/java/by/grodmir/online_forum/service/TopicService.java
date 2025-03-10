package by.grodmir.online_forum.service;

import by.grodmir.online_forum.dto.topic.CreateAndUpdateTopicDto;
import by.grodmir.online_forum.dto.topic.TopicDto;
import by.grodmir.online_forum.entity.Topic;
import by.grodmir.online_forum.entity.User;
import by.grodmir.online_forum.mapper.TopicMapper;
import by.grodmir.online_forum.repository.TopicRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
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
        Topic topic = topicMapper.toEntity(createTopicDto, user);
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
        topicMapper.updateFromDto(updateTopicDto, topic);
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
}
