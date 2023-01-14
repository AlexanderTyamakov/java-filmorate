package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validation;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
public class UserController {

    private Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @PostMapping("users")
    public User create(@Valid @RequestBody User user) throws ValidationException {
        Validation.validate(user);
        log.info("Пользователь добавлен в коллекцию");
        user.setId(id);
        users.put(id, user);
        id++;
        return user;
    }

    @PutMapping("users")
    public User update(@Valid @RequestBody User user) throws UserNotFoundException, ValidationException {
        Validation.validate(user);
        if (users.containsKey(user.getId())) {
            log.info("Пользователь обновлен в коллекции");
            users.put(user.getId(), user);
        } else {
            log.warn("Пользователь в коллекции не найден");
            throw new UserNotFoundException("Пользователь c id = " + user.getId() + " не найден");
        }
        return user;
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        log.info("Возвращен список пользователей");
        return users.values();
    }
}
