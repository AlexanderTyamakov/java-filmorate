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
import ru.yandex.practicum.filmorate.storage.database.interfaces.LikeStorage;
import ru.yandex.practicum.filmorate.storage.database.interfaces.UserDStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FilmorateApplication.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class LikeDbStorageTest {
    private final FilmDStorage filmStorage;
    private final UserDStorage userStorage;
    private final LikeStorage likeStorage;

    @Test
    void saveLikes() {
        userStorage.add(getExpUser1());
        Film film1 = getExpFilm1();
        filmStorage.add(film1);
        film1.addUserLike(1);
        likeStorage.saveLikes(film1);
        Film film2 = getExpFilm2();
        film2.setId(1);
        likeStorage.loadLikes(film2);
        assertEquals(film1.getUsersLikes(),film2.getUsersLikes());
    }

    @Test
    void loadLikes() {
        userStorage.add(getExpUser1());
        Film film1 = getExpFilm1();
        filmStorage.add(film1);
        Film film2 = getExpFilm2();
        film2.addUserLike(1);
        film2.setId(1);
        likeStorage.loadLikes(film2);
        assertEquals(new HashSet<>(),film2.getUsersLikes());
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

    private User getExpUser1() {
        User user = new User();
        user.setEmail("EMAIL1@EMAIL.COM");
        user.setLogin("usr1");
        user.setName("User1");
        user.setBirthday(LocalDate.of(1987, 10, 1));
        return user;
    }
}