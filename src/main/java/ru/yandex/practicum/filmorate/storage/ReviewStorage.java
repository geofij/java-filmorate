package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {

    void create(Review data);

    void update(Review data);

    Review getById(Long id);

    List<Review> getByFilmId(Long id);

    List<Review> getAll();

    void delete(Review data);
}
