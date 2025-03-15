package by.grodmir.online_forum.controller;

import by.grodmir.online_forum.dto.comment.CommentDto;
import by.grodmir.online_forum.dto.comment.CreateAndUpdateCommentDto;
import by.grodmir.online_forum.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/topic/{topicId}")
    public CommentDto addComment(@PathVariable("topicId") Integer topicId,
                                                 @RequestBody CreateAndUpdateCommentDto createCommentDto) {
        return commentService.addComment(topicId, createCommentDto);
    }

    @GetMapping("/topic/{topicId}")
    public List<CommentDto> getCommentsByTopic(@PathVariable("topicId") Integer topicId) {
        return commentService.getCommentsByTopicId(topicId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("commentId") Integer commentId) {
        commentService.deleteComment(commentId);
    }

    @PutMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable("commentId") Integer commentId, @RequestBody CreateAndUpdateCommentDto createCommentDto) {
        return commentService.updateComment(commentId, createCommentDto);
    }
}
