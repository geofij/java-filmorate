package ru.yandex.practicum.filmorate.storage;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

public interface LikesStorage {
    void addLike(long filmId, long userId);

    boolean deleteLike(long filmId, long userId);

    List<SimpleEntry<Long, Long>> getAllData();
}
