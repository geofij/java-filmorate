package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController controller;

    @BeforeEach
    void init() {
       controller = new UserController();
    }

    @Test
    void validateNullName() {
        User user = User.builder()
                .login("Login")
                .id(1L)
                .email("email@yandex.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        controller.validate(user);

        assertEquals(user.getLogin(), user.getName(), "Имя не соответствует логину.");
    }

    @Test
    void validateBlankName() {
        User user = User.builder()
                .login("Login")
                .id(1L)
                .name("")
                .email("email@yandex.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        controller.validate(user);

        assertEquals(user.getLogin(), user.getName(), "Имя не соответствует логину.");
    }

    @Test
    void validateWithName() {
        User user = User.builder()
                .login("Login")
                .name("Name")
                .id(1L)
                .email("email@yandex.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        controller.validate(user);

        assertNotEquals(user.getLogin(), user.getName(), "Имя соответствует логину.");
    }
}