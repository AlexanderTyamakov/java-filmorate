package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController (FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("films")
    public Film create(@Valid @RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping("films")
    public Film update(@Valid @RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping("/films/{id}")
    public Film findFilm(@PathVariable("id") Integer id) {
        return filmService.getById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film addLikeFromUser(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        return filmService.addUserLike(id,userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLikeFromUser(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        return filmService.deleteUserLike(id,userId);
    }

    @GetMapping("/films/popular")
    public Collection<Film> getPopularFilms(@RequestParam(required = false) final Integer count) {
        return filmService.returnPopularFilms(count);
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
