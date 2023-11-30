package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface ReviewStorage {

    Review create(Review data);

    Review update(Review data);

    Review getById(Long id);

    List<Review> getByFilmId(Long id, Integer count);

    List<Review> getAll(Integer count);

    void delete(Long id);

    void addReaction(Review review, Boolean isPositive, User user);

    void delReaction(Review review, User user);

}
