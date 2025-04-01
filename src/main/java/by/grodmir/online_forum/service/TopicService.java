package by.grodmir.online_forum.service;

import by.grodmir.online_forum.dto.topic.CreateAndUpdateTopicDto;
import by.grodmir.online_forum.dto.topic.TopicDto;
import by.grodmir.online_forum.entity.Topic;
import by.grodmir.online_forum.entity.User;
import by.grodmir.online_forum.mapper.TopicMapper;
import by.grodmir.online_forum.repository.TopicRepository;
import by.grodmir.online_forum.service.kafka.TopicEventService;
import by.grodmir.online_forum.validator.TopicValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final TopicValidator topicValidator;
    private final TopicEventService topicEventService;

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

        topicEventService.publishTopicEvent(topic, "TopicCreated");

        return topicMapper.toDto(topic);
    }

    @Transactional(readOnly = true)
    public TopicDto getTopicById(Integer id) {
        return topicMapper.toDto(findTopicById(id));
    }

    @Transactional
    public TopicDto updateTopic(Integer id, CreateAndUpdateTopicDto updateTopicDto) {
        Topic topic = findTopicById(id);
        topicValidator.validateTopicOwner(topic);
        topicMapper.updateFromDto(updateTopicDto, topic);

        topicEventService.publishTopicEvent(topic, "TopicUpdated");

        return topicMapper.toDto(topic);
    }

    @Transactional
    public void deleteTopic(Integer id) {
        Topic topic = findTopicById(id);
        topicValidator.validateTopicOwner(topic);
        topicRepository.delete(topic);
    }

    private Topic findTopicById(Integer id) {
        return topicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found with id: " + id));
    }
}
