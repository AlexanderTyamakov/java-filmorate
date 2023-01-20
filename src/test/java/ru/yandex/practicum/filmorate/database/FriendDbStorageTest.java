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
import ru.yandex.practicum.filmorate.storage.database.interfaces.FriendStorage;
import ru.yandex.practicum.filmorate.storage.database.interfaces.UserDStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = FilmorateApplication.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class FriendDbStorageTest {
    private static final String EMAIL1 = "user1@ya.ru";
    private static final String EMAIL2 = "user2@ya.ru";
    private final UserDStorage userStorage;
    private final FriendStorage friendStorage;

    @Test
    void insertFriendship(){
        User user1 = getExpUser1();
        userStorage.add(user1);
        User user2 = getExpUser2();
        userStorage.add(user2);
        friendStorage.insertFriendship(user1.getId(),user2.getId());
        friendStorage.loadFriends(user1);
        assertTrue(user1.getFriends().contains(user2.getId()));
    }

    @Test
    void removeFriendship(){
        User user1 = getExpUser1();
        userStorage.add(user1);
        User user2 = getExpUser2();
        userStorage.add(user2);
        friendStorage.insertFriendship(user1.getId(),user2.getId());
        friendStorage.removeFriendship(user1.getId(),user2.getId());
        friendStorage.loadFriends(user1);
        assertFalse(user1.getFriends().contains(user2.getId()));
    }

    @Test
    void updateFriendship(){
        User user1 = getExpUser1();
        userStorage.add(user1);
        User user2 = getExpUser2();
        userStorage.add(user2);
        friendStorage.insertFriendship(user1.getId(),user2.getId());
        friendStorage.updateFriendship(user1.getId(),user2.getId(),true, user1.getId(),user2.getId());
        friendStorage.loadFriends(user1);
        friendStorage.loadFriends(user2);
        System.out.println(user1);
        System.out.println(user2);
        assertTrue(user1.getFriends().contains(user2.getId()));
        assertTrue(user2.getFriends().contains(user1.getId()));
    }

    @Test
    void containsFriendship(){
        User user1 = getExpUser1();
        userStorage.add(user1);
        User user2 = getExpUser2();
        userStorage.add(user2);
        friendStorage.insertFriendship(user1.getId(),user2.getId());
        assertFalse(friendStorage.containsFriendship(user1.getId(),user2.getId(),true));
        friendStorage.updateFriendship(user1.getId(),user2.getId(),true, user1.getId(),user2.getId());
        assertTrue(friendStorage.containsFriendship(user1.getId(),user2.getId(),true));
    }

    @Test
    void loadFriends(){
        User user1 = getExpUser1();
        userStorage.add(user1);
        User user2 = getExpUser2();
        userStorage.add(user2);
        friendStorage.insertFriendship(user1.getId(),user2.getId());
        assertFalse(user1.getFriends().contains(user2.getId()));
        friendStorage.loadFriends(user1);
        assertTrue(user1.getFriends().contains(user2.getId()));
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

    private User getExpUser1() {
        User user = new User();
        user.setEmail(EMAIL1);
        user.setLogin("usr1");
        user.setName("User1");
        user.setBirthday(LocalDate.of(1987, 10, 1));
        return user;
    }

    private User getExpUser2() {
        User user = new User();
        user.setEmail(EMAIL2);
        user.setLogin("usr2");
        user.setName("User2");
        user.setBirthday(LocalDate.of(1990, 5, 6));
        return user;
    }
}