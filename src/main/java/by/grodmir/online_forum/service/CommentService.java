package by.grodmir.online_forum.service;

import by.grodmir.online_forum.dto.comment.CommentDto;
import by.grodmir.online_forum.dto.comment.CreateAndUpdateCommentDto;
import by.grodmir.online_forum.entity.Comment;
import by.grodmir.online_forum.entity.Topic;
import by.grodmir.online_forum.entity.User;
import by.grodmir.online_forum.repository.CommentRepository;
import by.grodmir.online_forum.repository.TopicRepository;
import by.grodmir.online_forum.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TopicRepository topicRepository;
    private final NotificationService notificationService;

    public CommentDto addComment(Integer topicId, CreateAndUpdateCommentDto createCommentDto) {
        User user = getCurrentUser();
        Topic topic = findTopicById(topicId);

        Comment comment = buildComment(user, topic, createCommentDto);
        commentRepository.save(comment);

        sendNotificationIfNeeded(topic, user);

        return mapToDto(comment);
    }

    public List<CommentDto> getCommentsByTopicId(Integer topicId) {
        return commentRepository.findByTopicId(topicId).stream()
                .map(this::mapToDto)
                .toList();
    }

    public void deleteComment(Integer commentId) {
        User user = getCurrentUser();
        Comment comment = findCommentById(commentId);

        checkCommentOwnership(comment, user.getUsername());
        commentRepository.delete(comment);
    }

    public CommentDto updateComment(Integer commentId, CreateAndUpdateCommentDto updateCommentDto) {
        User user = getCurrentUser();

        Comment comment = findCommentById(commentId);

        checkCommentOwnership(comment, user.getUsername());

        comment.setContent(updateCommentDto.getContent());
        commentRepository.save(comment);

        return mapToDto(comment);
    }

    private CommentDto mapToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getAuthor().getUsername(), comment.getContent(), comment.getCreatedAt());
    }

    private Comment buildComment(User user, Topic topic, CreateAndUpdateCommentDto createCommentDto) {
        return Comment.builder()
                .content(createCommentDto.getContent())
                .author(user)
                .topic(topic)
                .build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found with name: " + authentication.getName()));
    }

    private Topic findTopicById(Integer topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found with id: " + topicId));
    }

    private Comment findCommentById(Integer commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));
    }

    private void checkCommentOwnership(Comment comment, String currentUsername) {
        if (!currentUsername.equals(comment.getAuthor().getUsername())) {
            throw new AccessDeniedException("You don't have permission to perform this action");
        }
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
