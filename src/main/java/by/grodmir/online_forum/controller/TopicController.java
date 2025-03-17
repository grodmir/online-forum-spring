package by.grodmir.online_forum.controller;

import by.grodmir.online_forum.dto.topic.CreateAndUpdateTopicDto;
import by.grodmir.online_forum.dto.topic.TopicDto;
import by.grodmir.online_forum.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topics")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @GetMapping
    public List<TopicDto> getAllTopics() {
        return topicService.getAllTopics();
    }

    @PostMapping
    public TopicDto createTopic(@RequestBody @Valid CreateAndUpdateTopicDto topicDto) {
        return topicService.createTopic(topicDto);
    }

    @GetMapping("/{id}")
    public TopicDto getTopicById(@PathVariable Integer id) {
        return topicService.getTopicById(id);
    }

    @PutMapping("/{id}")
    public TopicDto updateTopic(@PathVariable Integer id,
                                                @RequestBody @Valid CreateAndUpdateTopicDto topicDto) {
        return topicService.updateTopic(id, topicDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTopic(@PathVariable Integer id) {
        topicService.deleteTopic(id);
    }
}
