package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Validation;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
public class FilmController {
    private Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @PostMapping("films")
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        Validation.validate(film);
        log.info("Фильм добавлен в коллекцию");
        film.setId(id);
        films.put(id, film);
        id++;
        return film;
    }

    @PutMapping("films")
    public Film update(@Valid @RequestBody Film film) throws ValidationException, FilmNotFoundException {
        Validation.validate(film);
        if (films.containsKey(film.getId())) {
            log.info("Фильм обновлен в коллекции");
            films.put(film.getId(), film);
        } else {
            log.warn("Фильм в коллекции не найден");
            throw new FilmNotFoundException("Фильм id = " + film.getId() + " не найден");
        }
        return film;
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        log.info("Возвращен список фильмов");
        return films.values();
    }
}
