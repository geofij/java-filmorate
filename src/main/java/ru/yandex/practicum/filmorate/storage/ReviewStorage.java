package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {

    Review create(Review data);

    Review update(Review data);

    Review getById(Long id);

    List<Review> getByFilmId(Long id, Integer count);

    List<Review> getAll(Integer count);

    void delete(Long id);

    void addReaction(Long reviewId, Boolean isPositive, Long userId);

    void delReaction(Long reviewId, Long userId);

}
