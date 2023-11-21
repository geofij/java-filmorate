package ru.yandex.practicum.filmorate.storage;

public interface LikesStorage {
    void addLike(long filmId, long userId);
    boolean deleteLike(long filmId, long userId);
}
