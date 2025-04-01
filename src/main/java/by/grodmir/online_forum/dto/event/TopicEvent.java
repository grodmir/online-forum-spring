package by.grodmir.online_forum.dto.event;

import by.grodmir.online_forum.dto.topic.TopicDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicEvent {
    private String eventType;
    private TopicDto payload;
}
