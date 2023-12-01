package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.feed.Event;
import ru.yandex.practicum.filmorate.model.feed.Feed;
import ru.yandex.practicum.filmorate.model.feed.Operation;
import ru.yandex.practicum.filmorate.storage.FeedStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FeedDbStorage implements FeedStorage {
    private final JdbcTemplate jdbcTemplate;

    private static Feed createFeed(ResultSet rs, int rowNum) throws SQLException {
        return Feed.builder()
                .eventId(rs.getLong("EVENT_ID"))
                .userId(rs.getLong("USER_ID"))
                .eventType(Event.valueOf(rs.getString("EVENT_TYPE")))
                .operation(Operation.valueOf(rs.getString("OPERATION")))
                .entityId(rs.getLong("ENTITY_ID"))
                .timestamp(rs.getTimestamp("TIMESTAMP").getTime())
                .build();
    }

    public void addFeed(long userId, Event eventType, Operation operation, long entityId) {
        Feed feed = new Feed(userId, eventType, operation, entityId);

        String sqlQuery = "INSERT INTO FEED(USER_ID, EVENT_TYPE, OPERATION, ENTITY_ID, TIMESTAMP)" +
                "values (?, ?, ?, ?, ?)";

        try {
            jdbcTemplate.update(sqlQuery,
                    feed.getUserId(),
                    feed.getEventType().name(),
                    feed.getOperation().name(),
                    feed.getEntityId(),
                    new Timestamp(System.currentTimeMillis())); // или используйте другой метод для получения времени
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public Feed getFeedByEventId(long eventId) {
        String sqlQuery = "SELECT * FROM FEED WHERE EVENT_ID = ?";
        List<Feed> feeds = jdbcTemplate.query(sqlQuery, FeedDbStorage::createFeed, eventId);
        if (feeds.size() != 1) {
            throw new DataNotFoundException("Feed with event ID-" + eventId + " not found");
        } else return feeds.get(0);
    }

    @Override
    public List<Feed> getFeedByUserid(long userId) {
        String sqlQuery = "SELECT * FROM FEED WHERE USER_ID = ?";
        List<Feed> feeds = jdbcTemplate.query(sqlQuery, FeedDbStorage::createFeed, userId);
        if (feeds.isEmpty()) {
            throw new DataNotFoundException("Feed with event ID-" + userId + " not found");
        } else return feeds;
    }

    @Override
    public LinkedHashSet<Feed> getAll() {
        String sqlQuery = "SELECT * FROM FEED ORDER BY TIMESTAMP";
        return new LinkedHashSet<>(jdbcTemplate.query(sqlQuery, FeedDbStorage::createFeed));
    }
}
