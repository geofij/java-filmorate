package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.feed.Feed;
import ru.yandex.practicum.filmorate.storage.db.FeedDbStorage;

import java.util.LinkedHashSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedDbStorage feedDbStorage;

    public LinkedHashSet<Feed> getFeedByUserid(long userId) {
        log.info("Return feeds for user: {}", userId);
        return feedDbStorage.getFeedByUserid(userId);
    }

    public LinkedHashSet<Feed> getAll() {
        return feedDbStorage.getAll();
    }
}
