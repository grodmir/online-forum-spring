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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("User not found with name: " + authentication.getName()));

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Topic not found with id: " + topicId));

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setTopic(topic);
        comment.setContent(createCommentDto.getContent());
        commentRepository.save(comment);

        if (!topic.getUser().getUsername().equals(user.getUsername())) {
            notificationService.sendNotification(
                    topic.getUser().getUsername(),
                    "💬 User @" + user.getUsername() + " left a comment in your topic."
            );
        }

        return new CommentDto(comment.getId(), user.getUsername(), comment.getContent(), comment.getCreatedAt());
    }

    public List<CommentDto> getCommentsByTopicId(Integer topicId) {
        return commentRepository.findByTopicId(topicId).stream()
                .map(this::mapToDto)
                .toList();
    }

    public void deleteComment(Integer commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));

        if (!currentUsername.equals(comment.getAuthor().getUsername())) {
            throw new AccessDeniedException("You can't delete this comment");
        }

        commentRepository.delete(comment);
    }

    public CommentDto updateComment(Integer commentId, CreateAndUpdateCommentDto updateCommentDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found with id: " + commentId));

        if (!currentUsername.equals(comment.getAuthor().getUsername())) {
            throw new AccessDeniedException("You cannot edit this comment");
        }

        comment.setContent(updateCommentDto.getContent());
        commentRepository.save(comment);

        return mapToDto(comment);
    }

    private CommentDto mapToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getAuthor().getUsername(), comment.getContent(), comment.getCreatedAt());
    }
}
