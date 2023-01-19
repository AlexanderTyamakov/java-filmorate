package ru.yandex.practicum.filmorate.database;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.database.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserDbStorageTest {
    private final UserDbStorage userStorage;
    private static final String EMAIL1 = "user1@ya.ru";
    private static final String EMAIL2 = "user2@ya.ru";

    @Test
    void add() {
        User expUser = getExpUser1();
        userStorage.add(expUser);
        User actUser = userStorage.getById(expUser.getId());
        assertEquals(expUser.getId(),actUser.getId());
        assertEquals(expUser.getName(),actUser.getName());
        assertEquals(expUser.getEmail(), actUser.getEmail());
        assertEquals(expUser.getLogin(), actUser.getLogin());
        assertEquals(expUser.getBirthday(), actUser.getBirthday());
    }

    @Test
    void replace() {
        User expUser = getExpUser1();
        userStorage.add(expUser);
        expUser.setName("Super User");

        userStorage.replace(expUser);
        User actUser = userStorage.getById(expUser.getId());

        assertEquals(expUser.getId(), actUser.getId());
        assertEquals(expUser.getName(), actUser.getName());
    }

    @Test
    void getById() {
        User expUser = getExpUser1();
        userStorage.add(expUser);
        User actUser = userStorage.getById(expUser.getId());
        assertEquals(expUser.getId(), actUser.getId());
        assertEquals(expUser.getName(), actUser.getName());
    }

    @Test
    void getValues() {
        User expUser1 = getExpUser1();
        userStorage.add(expUser1);
        User expUser2 = getExpUser2();
        userStorage.add(expUser2);
        List<User> expUsers = List.of(expUser1, expUser2);
        Collection<User> actUsers = userStorage.getValues();
        assertEquals(2, actUsers.size());
    }

//    @Test
//    void delete(){}
//
//    @Test
//    void insertFriendship(){}
//
//    @Test
//    void removeFriendship(){}
//
//    @Test
//    void updateFriendship(){}
//
//    @Test
//    void containsFriendship(){}
//
//    @Test
//    void loadFriends(){}

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