package by.grodmir.online_forum.service.kafka;

import by.grodmir.online_forum.dto.event.CommentEvent;
import by.grodmir.online_forum.dto.event.TopicEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaEventPublisher {
    private final KafkaTemplate<String, TopicEvent> topicKafkaTemplate;
    private final KafkaTemplate<String, CommentEvent> commentKafkaTemplate;

    public void publishTopicEvent(TopicEvent topicEvent) {
        topicKafkaTemplate.send("topics-events", topicEvent);
    }

    public void publishCommentEvent(CommentEvent commentEvent) {
        commentKafkaTemplate.send("comments-events", commentEvent);
    }
}
