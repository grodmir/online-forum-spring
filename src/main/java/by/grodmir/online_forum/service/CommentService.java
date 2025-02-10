package by.grodmir.online_forum.service;

import by.grodmir.online_forum.dtos.comment.CommentDto;
import by.grodmir.online_forum.dtos.comment.CreateAndUpdateCommentDto;
import by.grodmir.online_forum.entities.Comment;
import by.grodmir.online_forum.entities.Topic;
import by.grodmir.online_forum.entities.User;
import by.grodmir.online_forum.repositories.CommentRepository;
import by.grodmir.online_forum.repositories.TopicRepository;
import by.grodmir.online_forum.repositories.UserRepository;
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
                .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден c именем: " + authentication.getName()));

        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new EntityNotFoundException("Топик не найден c id: " + topicId));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setTopic(topic);
        comment.setContent(createCommentDto.getContent());
        commentRepository.save(comment);

        if (!topic.getUser().getUsername().equals(user.getUsername())) {
            notificationService.sendNotification(
                    topic.getUser().getUsername(),
                    "💬 Пользователь @" + user.getUsername() + " оставил комментарий в вашем топике."
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
                .orElseThrow(() -> new EntityNotFoundException("Комментарий не найден с id: " + commentId));

        if (!currentUsername.equals(comment.getUser().getUsername())) {
            throw new AccessDeniedException("Вы не можете удалить этот комментарий");
        }

        commentRepository.delete(comment);
    }

    public CommentDto updateComment(Integer commentId, CreateAndUpdateCommentDto updateCommentDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Комментарий не найден с id: " + commentId));

        if (!currentUsername.equals(comment.getUser().getUsername())) {
            throw new AccessDeniedException("Вы не можете редактировать этот комментарий");
        }

        comment.setContent(updateCommentDto.getContent());
        commentRepository.save(comment);

        return mapToDto(comment);
    }

    private CommentDto mapToDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getUser().getUsername(), comment.getContent(), comment.getCreatedAt());
    }
}
