package by.grodmir.online_forum.controllers;

import by.grodmir.online_forum.dtos.comment.CommentDto;
import by.grodmir.online_forum.dtos.comment.CreateAndUpdateCommentDto;
import by.grodmir.online_forum.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/topic/{topicId}")
    public ResponseEntity<CommentDto> addComment(@PathVariable("topicId") Integer topicId,
                                                 @RequestBody CreateAndUpdateCommentDto createCommentDto) {
        return ResponseEntity.ok(commentService.addComment(topicId, createCommentDto));
    }

    /**
     * Возвращает пустой json если комментариев нету (остальное дело фронта)
     * */
    @GetMapping("/topic/{topicId}")
    public ResponseEntity<List<CommentDto>> getCommentsByTopic(@PathVariable("topicId") Integer topicId) {
        return ResponseEntity.ok(commentService.getCommentsByTopicId(topicId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Integer commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable("commentId") Integer commentId, @RequestBody CreateAndUpdateCommentDto createCommentDto) {
        return ResponseEntity.ok(commentService.updateComment(commentId, createCommentDto));
    }
}
