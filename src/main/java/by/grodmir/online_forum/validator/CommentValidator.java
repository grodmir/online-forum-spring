package by.grodmir.online_forum.validator;

import by.grodmir.online_forum.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentValidator {

    public void validateCommentOwnership(Comment comment, String currentUsername) {
        if (!currentUsername.equals(comment.getAuthor().getUsername())) {
            throw new AccessDeniedException("You don't have permission to perform this action");
        }
    }
}
