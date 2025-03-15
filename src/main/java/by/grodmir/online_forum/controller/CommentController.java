package by.grodmir.online_forum.controller;

import by.grodmir.online_forum.dto.comment.CommentDto;
import by.grodmir.online_forum.dto.comment.CreateAndUpdateCommentDto;
import by.grodmir.online_forum.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/topic/{id}")
    public CommentDto addComment(@PathVariable("id") Integer topicId,
                                                 @RequestBody @Valid CreateAndUpdateCommentDto commentDto) {
        return commentService.addComment(topicId, commentDto);
    }

    @GetMapping("/topic/{id}")
    public List<CommentDto> getCommentsByTopic(@PathVariable("id") Integer id) {
        return commentService.getCommentsByTopicId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("id") Integer id) {
        commentService.deleteComment(id);
    }

    @PutMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable("commentId") Integer id, @RequestBody @Valid CreateAndUpdateCommentDto commentDto) {
        return commentService.updateComment(id, commentDto);
    }
}
