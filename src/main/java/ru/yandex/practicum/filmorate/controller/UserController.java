package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;
    private long idCounter;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validate(user);
        ++idCounter;
        user.setId(idCounter);
        log.info("Creating user {}", user);
        service.create(user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        validate(user);
        log.info("Updating user {}", user);
        service.update(user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        log.info("Getting all users");
        return service.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") long id) {
        log.info("Getting user id-{}", id);
        return service.get(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        log.info("Users id-{} and id-{} become friends", id, friendId);
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public boolean deleteFriend(@PathVariable("id") long id, @PathVariable("friendId") long friendId) {
        log.info("Users id-{} and id-{} break friendship", id, friendId);
        return service.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") long id) {
        log.info("Getting user's id-{} friend list ", id);
        return service.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") long id, @PathVariable("otherId") long otherId) {
        log.info("Getting id-{} and id-{} common friends.", id, otherId);
        return service.getCommonFriends(id, otherId);
    }

    public void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
