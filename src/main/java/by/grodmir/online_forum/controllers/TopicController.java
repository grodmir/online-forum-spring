package by.grodmir.online_forum.controllers;

import by.grodmir.online_forum.dtos.CreateTopicDto;
import by.grodmir.online_forum.dtos.TopicDto;
import by.grodmir.online_forum.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @GetMapping
    public ResponseEntity<List<TopicDto>> getAllTopics() {
        return ResponseEntity.ok(topicService.getAllTopics());
    }

    @PostMapping
    public ResponseEntity<TopicDto> createTopic(@RequestBody CreateTopicDto createTopicDto) {
        return ResponseEntity.ok(topicService.createTopic(createTopicDto));
    }
}
