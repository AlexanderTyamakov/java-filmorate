package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController (UserService userService) {
        this.userService = userService;
    }

    @PostMapping("users")
    public User create(@Valid @RequestBody User user) throws ValidationException {
        return userService.create(user);
    }

    @PutMapping("users")
    public User update(@Valid @RequestBody User user) throws UserNotFoundException, ValidationException {
        return userService.update(user);
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/users/{id}")
    public User findUser(@PathVariable("id") Integer id) throws UserNotFoundException {
        return userService.getById(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) throws UserNotFoundException {
        return userService.addFriend(id,friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public User deleteFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) throws UserNotFoundException {
        return userService.deleteFriend(id,friendId);
    }

    @GetMapping("/users/{id}/friends")
    public Collection<User> findUserFriends(@PathVariable("id") Integer id) throws UserNotFoundException {
        return userService.returnFriendCollection(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public Collection<User> findUsersCommonFriends(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId) throws UserNotFoundException {
        return userService.returnCommonFriends(id,otherId);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final ValidationException e) {
        return new ErrorResponse(
                "Ошибка в отправленных данных", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final FilmNotFoundException e) {
        return new ErrorResponse(
                "Фильм не найден", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final UserNotFoundException e) {
        return new ErrorResponse(
                "Пользователь не найден", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(final Exception e) {
        return new ErrorResponse(
                "Ошибка", e.getMessage()
        );
    }
}
