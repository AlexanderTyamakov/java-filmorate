package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class Validation {

    public static void validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата релиза фильма раньше 28 декабря 1895 года: " + film.getReleaseDate());
            throw new ValidationException("Не пройдена валидация фильма");
        }
    }

    public static void validate(User user) throws ValidationException {
        if (user.getLogin().contains(" ")) {
            log.warn("В логине пользователя есть пробел");
            throw new ValidationException("Не пройдена валидация пользователя по логину: " + user.getLogin());
        }
        if ((user.getName() == null) || (user.getName().isBlank())) {
            log.info("Пользователю в качестве имени присвоен логин");
            user.setName(user.getLogin());
        }
    }
}
