package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.RatingNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.Collection;


    @Slf4j
    @RestController
    public class RatingController {

        private final RatingService ratingService;

        @Autowired
        public RatingController (RatingService ratingService) {
            this.ratingService = ratingService;
        }

        @GetMapping("/mpa")
        public Collection<Rating> findAll() {
            return ratingService.getRatings();
        }

        @GetMapping("/mpa/{id}")
        public Rating findRating(@PathVariable("id") Integer id) {
            return ratingService.getRatingById(id);
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
        public ErrorResponse handle(final RatingNotFoundException e) {
            return new ErrorResponse(
                    "Рейтинг не найден", e.getMessage()
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

