package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FilmorateApplicationTests {

    static UserService userService = new UserService(new InMemoryUserStorage());
    static FilmService filmService = new FilmService(new InMemoryFilmStorage(),new InMemoryUserStorage());

    @Test
    void contextLoads() {
    }

    @Test
    public void userValidateLoginTest() throws ValidationException {
        User user = new User("mail@mail.ru", "dolore ullamco", LocalDate.now());
        Assertions.assertThrows(ValidationException.class, () -> userService.validate(user));
    }

    @Test
    public void userValidateNameTest() throws ValidationException {
        User user = new User("mail@mail.ru", "dolore", LocalDate.now());
        userService.validate(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    public void filmValidateReleaseDateTest() {
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1890, 1, 1), 1);
        Assertions.assertThrows(ValidationException.class, () -> filmService.validate(film));
    }
}
