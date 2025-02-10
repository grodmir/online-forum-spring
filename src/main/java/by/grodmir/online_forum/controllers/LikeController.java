package by.grodmir.online_forum.controllers;

import by.grodmir.online_forum.dtos.like.LikeDto;
import by.grodmir.online_forum.entities.EntityType;
import by.grodmir.online_forum.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/{entityType}/{entityId}")
    public ResponseEntity<LikeDto> toggleLike(
            @PathVariable EntityType entityType,
            @PathVariable Integer entityId,
            @RequestParam boolean isLike
    ) {
        return ResponseEntity.ok(likeService.toggleLike(entityId, entityType, isLike));
    }

    @GetMapping("/{entityType}/{entityId}/count")
    public ResponseEntity<Map<String, Integer>> getLikeCount(
            @PathVariable EntityType entityType,
            @PathVariable Integer entityId
    ) {
        Map<String, Integer> response = Map.of(
                "likes", likeService.countLikes(entityId, entityType),
                "dislikes", likeService.countDislikes(entityId, entityType)
        );
        return ResponseEntity.ok(response);
    }
}
