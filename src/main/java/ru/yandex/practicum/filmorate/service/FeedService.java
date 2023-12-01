package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.feed.Feed;
import ru.yandex.practicum.filmorate.storage.db.FeedDbStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {
    private final FeedDbStorage feedDbStorage;

    public List<Feed> getFeedByUserid(long userId) {
        log.info("Return feeds for user: {}", userId);
        return feedDbStorage.getFeedByUserid(userId);
    }
}
