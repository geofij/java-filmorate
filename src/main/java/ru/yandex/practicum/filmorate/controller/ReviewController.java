package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public Review create(@Valid @RequestBody Review review) {
        Review ra = reviewService.create(review);
        log.info("Creating review {}", ra);
        return ra;
    }

    @PutMapping
    public Review update(@Valid @RequestBody Review review) {
        Review ra = reviewService.update(review);
        log.info("Updating review {}", ra);
        return ra;
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable("id") Long id) {
        Boolean ra = reviewService.delete(id);
        log.info("Updating review {}", ra);
        return ra;
    }

    @GetMapping("/{id}")
    public Review getById(@PathVariable("id") Long id) {
        Review ra = reviewService.getById(id);
        log.info("Updating review {}", ra);
        return ra;
    }

    @GetMapping
    public List<Review> getAny(@RequestParam(defaultValue = "0") @Min(0) Long filmId,
                               @RequestParam(defaultValue = "10") @Min(1) Integer count) {
        if (filmId == 0) return reviewService.getAll(count);
        return reviewService.getByFilmId(filmId, count);
    }

    @PutMapping("/{id}/like/{userId}")
    public Boolean addReviewLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        reviewService.addReaction(id, true, userId);
        return true;
    }

    @PutMapping("/{id}/dislike/{userId}")
    public Boolean addReviewDislike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        reviewService.addReaction(id, false, userId);
        return true;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Boolean delReviewLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        reviewService.delReaction(id, userId);
        return true;
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public Boolean delReviewDislike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        reviewService.delReaction(id, userId);
        return true;
    }
}
