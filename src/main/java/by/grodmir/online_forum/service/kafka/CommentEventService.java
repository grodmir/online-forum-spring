package by.grodmir.online_forum.service.kafka;

import by.grodmir.online_forum.dto.event.CommentEvent;
import by.grodmir.online_forum.entity.Comment;
import by.grodmir.online_forum.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentEventService {
    private final CommentMapper commentMapper;
    private final KafkaEventPublisher kafkaEventPublisher;

    public void publishCommentEvent(Comment comment, String eventType) {
        CommentEvent event = CommentEvent.builder()
                .eventType(eventType)
                .payload(commentMapper.toDto(comment))
                .build();
        kafkaEventPublisher.publishCommentEvent(event);
    }
}
