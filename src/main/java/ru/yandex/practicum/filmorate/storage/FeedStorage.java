package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.feed.Event;
import ru.yandex.practicum.filmorate.model.feed.Feed;
import ru.yandex.practicum.filmorate.model.feed.Operation;

import java.util.LinkedHashSet;

public interface FeedStorage {

    void addFeed(long userId, Event eventType, Operation operation, long entityId);

    Feed getFeedByEventId(long eventId);

    LinkedHashSet<Feed> getFeedByUserid(long userId);

    LinkedHashSet<Feed> getAll();
}
