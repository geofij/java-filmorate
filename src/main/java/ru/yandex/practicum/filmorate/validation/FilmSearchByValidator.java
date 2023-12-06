package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FilmSearchByValidator implements ConstraintValidator<SearchByConstraint, String> {

    public static final String SEARCH_BY_DEFAULT = "title,director";
    public static final String SEARCH_BY_TITLE = "title";
    public static final String SEARCH_BY_DIRECTOR = "director";

    @Override
    public void initialize(SearchByConstraint searchByConstraint) {
    // Возможно пустой метод необязателен, но я нашёл решение именно с ним
    }

    @Override
    public boolean isValid(String searchBy, ConstraintValidatorContext ctx) {
        String searchByLowCase = searchBy.toLowerCase();
        return searchByLowCase.equals(SEARCH_BY_TITLE) ||
                searchByLowCase.equals(SEARCH_BY_DIRECTOR) ||
                searchByLowCase.equals(SEARCH_BY_TITLE + "," + SEARCH_BY_DIRECTOR) ||
                searchByLowCase.equals(SEARCH_BY_DIRECTOR + "," + SEARCH_BY_TITLE);
    }
}
