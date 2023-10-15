package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends BaseController<User> {
    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Creating user {}", user);
        return super.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Updating user {}", user);
        return super.update(user);
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Getting all users");
        return super.getAllData();
    }

    @Override
    public void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
