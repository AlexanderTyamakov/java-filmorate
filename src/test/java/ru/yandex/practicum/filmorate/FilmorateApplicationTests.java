package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validation;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmorateApplicationTests {
    @Test
    void contextLoads() {
    }

    @Test
    public void userValidateLoginTest() throws ValidationException {
        boolean thrown = false;
        User user = new User("mail@mail.ru", "dolore ullamco", LocalDate.now());
        try {
            Validation.validate(user);
        } catch (ValidationException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }

    @Test
    public void userValidateNameTest() throws ValidationException {
        User user = new User("mail@mail.ru", "dolore", LocalDate.now());
        Validation.validate(user);
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    public void filmValidateReleaseDateTest() {
        boolean thrown = false;
        Film film = new Film("nisi eiusmod", "adipisicing", LocalDate.of(1890, 1, 1), 1);
        try {
            Validation.validate(film);
        } catch (ValidationException e) {
            thrown = true;
        }
        assertTrue(thrown);
    }
}
