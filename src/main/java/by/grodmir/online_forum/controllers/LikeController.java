package by.grodmir.online_forum.controllers;

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
    public ResponseEntity<String> toggleLike(
            @PathVariable EntityType entityType,
            @PathVariable Integer entityId,
            @RequestParam boolean isLike
    ) {
        likeService.toggleLike(entityId, entityType, isLike);
        return ResponseEntity.ok().build();
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
