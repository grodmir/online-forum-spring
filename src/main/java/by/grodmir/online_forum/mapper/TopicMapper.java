package by.grodmir.online_forum.mapper;

import by.grodmir.online_forum.dto.topic.TopicDto;
import by.grodmir.online_forum.entity.Topic;
import by.grodmir.online_forum.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TopicMapper {
    public TopicDto toDto(Topic topic) {
        return new TopicDto(
                topic.getId(),
                topic.getTitle(),
                topic.getContent(),
                topic.getUser().getUsername(),
                topic.getCreated_at().toString()
        );
    }

    public Topic toEntity(TopicDto topicDto, User user) {
        return Topic.builder()
                .title(topicDto.getTitle())
                .content(topicDto.getContent())
                .user(user)
                .build();
    }
}
