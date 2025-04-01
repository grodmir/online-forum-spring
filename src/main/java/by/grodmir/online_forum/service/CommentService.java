package by.grodmir.online_forum.service;

import by.grodmir.online_forum.dto.comment.CommentDto;
import by.grodmir.online_forum.dto.comment.CreateAndUpdateCommentDto;
import by.grodmir.online_forum.entity.Comment;
import by.grodmir.online_forum.entity.Topic;
import by.grodmir.online_forum.entity.User;
import by.grodmir.online_forum.mapper.CommentMapper;
import by.grodmir.online_forum.repository.CommentRepository;
import by.grodmir.online_forum.repository.TopicRepository;
import by.grodmir.online_forum.service.kafka.CommentEventService;
import by.grodmir.online_forum.validator.CommentValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TopicRepository topicRepository;
    private final NotificationService notificationService;
    private final CommentMapper commentMapper;
    private final SecurityService securityService;
    private final CommentValidator commentValidator;
    private final CommentEventService commentEventService;

    public CommentDto createComment(Integer topicId, CreateAndUpdateCommentDto createCommentDto) {
        User user = securityService.getCurrentUser();
        Topic topic = findTopicById(topicId);

        Comment comment = commentMapper.toEntity(createCommentDto, user, topic);
        commentRepository.save(comment);

        sendNotificationIfNeeded(topic, user);

        commentEventService.publishCommentEvent(comment, "CommentCreated");

        return commentMapper.toDto(comment);
    }

    public List<CommentDto> getCommentsByTopicId(Integer topicId) {
        return commentRepository.findByTopicId(topicId).stream()
                .map(commentMapper::toDto)
                .toList();
    }

    public void deleteComment(Integer commentId) {
        User user = securityService.getCurrentUser();
        Comment comment = findCommentById(commentId);
        commentValidator.validateCommentOwnership(comment, user.getUsername());
        commentRepository.delete(comment);
    }

    public CommentDto updateComment(Integer commentId, CreateAndUpdateCommentDto updateCommentDto) {
        User user = securityService.getCurrentUser();
        Comment comment = findCommentById(commentId);
        commentValidator.validateCommentOwnership(comment, user.getUsername());
        comment.setContent(updateCommentDto.getContent());
        commentRepository.save(comment);

        commentEventService.publishCommentEvent(comment, "CommentUpdated");

        return commentMapper.toDto(comment);
    }

    private Topic findTopicById(Integer topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found with id: " + topicId));
    }

    private Comment findCommentById(Integer commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));
    }

    private void sendNotificationIfNeeded(Topic topic, User commentAuthor) {
        if (!topic.getUser().getUsername().equals(commentAuthor.getUsername())) {
            notificationService.sendNotification(
                    topic.getUser().getUsername(),
                    "💬 User @" + commentAuthor.getUsername() + " left a comment in your topic."
            );
        }
    }
}
