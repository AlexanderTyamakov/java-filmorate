package ru.yandex.practicum.filmorate.database;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.database.interfaces.FilmDStorage;
import ru.yandex.practicum.filmorate.storage.database.interfaces.UserDStorage;

import java.time.LocalDate;
import java.util.*;


import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FilmorateApplication.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class FilmDbStorageTest {
    private final FilmDStorage filmStorage;
    private final UserDStorage userStorage;

    @Test
    void add() {
        Film expFilm = getExpFilm1();
        filmStorage.add(expFilm);
        Film actFilm = filmStorage.getById(expFilm.getId());
        assertEquals(expFilm.getId(),actFilm.getId());
        assertEquals(expFilm.getName(),actFilm.getName());
        assertEquals(expFilm.getDescription(),actFilm.getDescription());
        assertEquals(expFilm.getReleaseDate(),actFilm.getReleaseDate());
        assertEquals(expFilm.getDuration(),actFilm.getDuration());
        assertEquals(expFilm.getMpa().getId(),actFilm.getMpa().getId());
    }

    @Test
    void replace() {
        Film expFilm = getExpFilm1();
        filmStorage.add(expFilm);
        expFilm.setName("Super Film");

        filmStorage.replace(expFilm);
        Film actFilm = filmStorage.getById(expFilm.getId());

        assertEquals(expFilm.getId(), actFilm.getId());
        assertEquals(expFilm.getName(), actFilm.getName());
    }

    @Test
    void getById() {
        Film expFilm = getExpFilm1();
        filmStorage.add(expFilm);
        Film actFilm = filmStorage.getById(expFilm.getId());
        assertEquals(expFilm.getId(), actFilm.getId());
        assertEquals(expFilm.getName(), actFilm.getName());
    }

    @Test
    void getValues() {
        Film expFilm1 = getExpFilm1();
        filmStorage.add(expFilm1);
        Film expFilm2 = getExpFilm2();
        filmStorage.add(expFilm2);
        Collection<Film> actFilms = (filmStorage.getValues());
        assertEquals(2, actFilms.size());
    }

    @Test
    void delete(){
        Film film1 = getExpFilm1();
        filmStorage.add(film1);
        filmStorage.delete(film1);
        Collection <Film> expected = new ArrayList<>();
        assertEquals(expected,filmStorage.getValues());
    }

    private Film getExpFilm1() {
        Film film = new Film();
        film.setId(1);
        film.setName("Film1");
        film.setDescription("DESCRIPTION1");
        film.setReleaseDate(LocalDate.of(2020, 3, 3));
        film.setDuration(100);

        Rating rating = new Rating();
        rating.setId(1);
        film.setMpa(rating);

        Genre genre1 = new Genre();
        genre1.setId(1);
        Genre genre2 = new Genre();
        genre2.setId(2);
        film.setGenres(Set.of(genre1, genre2));
        return film;
    }

    private Film getExpFilm2() {
        Film film = new Film();
        film.setId(2);
        film.setName("Film2");
        film.setDescription("DESCRIPTION2");
        film.setReleaseDate(LocalDate.of(2010, 1, 3));
        film.setDuration(90);
        Rating rating = new Rating();
        rating.setId(2);
        film.setMpa(rating);
        return film;
    }
}