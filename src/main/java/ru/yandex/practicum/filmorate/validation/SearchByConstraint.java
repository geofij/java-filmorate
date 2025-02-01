package ru.yandex.practicum.filmorate.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static ru.yandex.practicum.filmorate.validation.FilmSearchByValidator.SEARCH_BY_DIRECTOR;
import static ru.yandex.practicum.filmorate.validation.FilmSearchByValidator.SEARCH_BY_TITLE;

@Constraint(validatedBy = FilmSearchByValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SearchByConstraint {
    String message() default "Параметр by должен иметь значения " + SEARCH_BY_DIRECTOR + " или " + SEARCH_BY_TITLE;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
