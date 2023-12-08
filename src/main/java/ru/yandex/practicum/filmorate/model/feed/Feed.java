package ru.yandex.practicum.filmorate.model.feed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Feed {
    private long timestamp;
    private long userId;
    private Event eventType;
    private Operation operation;
    private long eventId;
    private long entityId;

    public Feed(long userId, Event eventType, Operation operation, long entityId) {
        this.userId = userId;
        this.eventType = eventType;
        this.operation = operation;
        this.entityId = entityId;
    }
}


