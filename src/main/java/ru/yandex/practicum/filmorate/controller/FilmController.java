package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RestController
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @PostMapping("films")
    public Film create(@RequestBody Film film) throws ValidationException {
        if ((film.getName() == null) || (film.getName().isBlank())) {
            log.warn("Пустое название фильма");
            throw new ValidationException("Не пройдена валидация фильма");
        } else if (film.getDescription().length() > 200) {
            log.warn("Максимальная длина описания фильма больше 200 символов");
            throw new ValidationException("Не пройдена валидация фильма");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата релиза фильма раньше 28 декабря 1895 года");
            throw new ValidationException("Не пройдена валидация фильма");
        } else if (film.getDuration() < 0) {
            log.warn("Продолжительность фильма отрицательная");
            throw new ValidationException("Не пройдена валидация фильма");
        } else {
            log.info("Фильм добавлен в коллекцию");
            film.setId(id);
            films.put(id, film);
            id++;
        }
        return film;
    }

    @PutMapping("films")
    public Film update(@RequestBody Film film) throws ValidationException, FilmNotFoundException {
        if ((film.getName() == null) || (film.getName().isBlank())
                || (film.getDescription().length() > 200)
                || (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
                || (film.getDuration() < 0)) {
            log.warn("Не пройдена валидация полей при обновлении/добавлении фильма");
            throw new ValidationException("Не пройдена валидация фильма");
        } else if (films.containsKey(film.getId())) {
            log.info("Фильм обновлен в коллекции");
            films.put(film.getId(), film);
        }
        else {
            log.warn("Фильм в коллекции не найден");
            throw new FilmNotFoundException("Фильм не найден");
        }
        return film;
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        log.info("Возвращен список фильмов");
        return films.values();
    }
}
