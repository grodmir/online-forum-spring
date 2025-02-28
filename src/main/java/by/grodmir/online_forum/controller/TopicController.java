package by.grodmir.online_forum.controller;

import by.grodmir.online_forum.dto.topic.CreateAndUpdateTopicDto;
import by.grodmir.online_forum.dto.topic.TopicDto;
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
    public ResponseEntity<TopicDto> createTopic(@RequestBody CreateAndUpdateTopicDto createTopicDto) {
        return ResponseEntity.ok(topicService.createTopic(createTopicDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TopicDto> getTopicById(@PathVariable Integer id) {
        return ResponseEntity.ok(topicService.getTopicById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicDto> updateTopic(@PathVariable Integer id,
                                                @RequestBody CreateAndUpdateTopicDto updateTopicDto) {
        return ResponseEntity.ok(topicService.updateTopic(id, updateTopicDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable Integer id) {
        topicService.deleteTopic(id);
        return ResponseEntity.noContent().build(); // 204 no content
    }
}
