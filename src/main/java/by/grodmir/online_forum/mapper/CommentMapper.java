package by.grodmir.online_forum.mapper;

import by.grodmir.online_forum.dto.comment.CommentDto;
import by.grodmir.online_forum.dto.comment.CreateAndUpdateCommentDto;
import by.grodmir.online_forum.entity.Comment;
import by.grodmir.online_forum.entity.Topic;
import by.grodmir.online_forum.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getAuthor().getUsername(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }

    public Comment toEntity(CreateAndUpdateCommentDto dto, User author, Topic topic) {
        return Comment.builder()
                .content(dto.getContent())
                .author(author)
                .topic(topic)
                .build();
    }
}
