package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

public interface ReviewStorage {

    void create(Review data);

    void update(Review data);

    Review getById(Long id);

    void delete(Long id);
}
