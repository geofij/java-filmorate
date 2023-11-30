package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;

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


}
