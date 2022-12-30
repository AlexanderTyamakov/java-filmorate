package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RestController
public class UserController {

    private Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @PostMapping("users")
    public User create(@RequestBody User user) throws ValidationException {
        if ((user.getName() == null) || (user.getName().isBlank())) {
            log.info("Пользователю в качестве имени присвоен логин");
            user.setName(user.getLogin());
        }
        if ((user.getEmail() == null) || (user.getEmail().isBlank()) || !(user.getEmail().contains("@"))) {
            log.warn("Электронная почта пользователя пустая или не содержит символ @");
            throw new ValidationException("Не пройдена валидация пользователя");
        } else if ((user.getLogin() == null) || (user.getLogin().contains(" "))) {
            log.warn("Логин пользователя пустой или содержит пробелы");
            throw new ValidationException("Не пройдена валидация пользователя");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения пользователя больше текущей даты");
            throw new ValidationException("Не пройдена валидация пользователя");
        } else {
            log.info("Пользователь добавлен в коллекцию");
            user.setId(id);
            users.put(id, user);
            id++;
        }
        return user;
    }

    @PutMapping("users")
    public User update(@RequestBody User user) throws ValidationException, UserNotFoundException {
        if ((user.getName() == null) || (user.getName().isBlank())) {
            log.info("Пользователю в качестве имени присвоен логин");
            user.setName(user.getLogin());
        }
        if ((user.getEmail() == null) || (user.getEmail().isBlank()) || !(user.getEmail().contains("@"))
                || ((user.getLogin() == null) || (user.getLogin().contains(" ")))
                || (user.getBirthday().isAfter(LocalDate.now()))) {
            log.warn("Не пройдена валидация полей при обновлении пользователя");
            throw new ValidationException("Не пройдена валидация пользователя");
        } else if (users.containsKey(user.getId())) {
            log.info("Пользователь обновлен в коллекции");
            users.put(user.getId(), user);
        } else {
            log.warn("Пользователь в коллекции не найден");
            throw new UserNotFoundException("Пользователь не найден");
        }
        return user;
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        log.info("Возвращен список пользователей");
        return users.values();
    }
}
