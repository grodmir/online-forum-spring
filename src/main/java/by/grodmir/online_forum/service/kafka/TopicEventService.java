package by.grodmir.online_forum.service.kafka;

import by.grodmir.online_forum.dto.event.TopicEvent;
import by.grodmir.online_forum.entity.Topic;
import by.grodmir.online_forum.mapper.TopicMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicEventService {
    private final TopicMapper topicMapper;
    private final KafkaEventPublisher kafkaEventPublisher;

    public void publishTopicEvent(Topic topic, String eventType) {
        TopicEvent event = TopicEvent.builder()
                .eventType(eventType)
                .payload(topicMapper.toDto(topic))
                .build();
        kafkaEventPublisher.publishTopicEvent(event);
    }
}
