package ru.yandex.practicum.filmorate.database;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.database.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.database.interfaces.UserDStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FilmorateApplication.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserDbStorageTest {
    private final UserDStorage userStorage;
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

    @Test
    void delete(){
        User user = getExpUser1();
        userStorage.add(user);
        userStorage.delete(user);
        Collection <User> expected  = new ArrayList<>();
        assertEquals(expected,userStorage.getValues());
    }

    @Test
    void insertFriendship(){
        User user1 = getExpUser1();
        userStorage.add(user1);
        User user2 = getExpUser2();
        userStorage.add(user2);
        userStorage.insertFriendship(user1.getId(),user2.getId());
        userStorage.loadFriends(user1);
        assertTrue(user1.getFriends().contains(user2.getId()));
    }

    @Test
    void removeFriendship(){
        User user1 = getExpUser1();
        userStorage.add(user1);
        User user2 = getExpUser2();
        userStorage.add(user2);
        userStorage.insertFriendship(user1.getId(),user2.getId());
        userStorage.removeFriendship(user1.getId(),user2.getId());
        userStorage.loadFriends(user1);
        assertFalse(user1.getFriends().contains(user2.getId()));
    }

    @Test
    void updateFriendship(){
        User user1 = getExpUser1();
        userStorage.add(user1);
        User user2 = getExpUser2();
        userStorage.add(user2);
        userStorage.insertFriendship(user1.getId(),user2.getId());
        userStorage.updateFriendship(user1.getId(),user2.getId(),true, user1.getId(),user2.getId());
        userStorage.loadFriends(user1);
        userStorage.loadFriends(user2);
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
        userStorage.insertFriendship(user1.getId(),user2.getId());
        assertFalse(userStorage.containsFriendship(user1.getId(),user2.getId(),true));
        userStorage.updateFriendship(user1.getId(),user2.getId(),true, user1.getId(),user2.getId());
        assertTrue(userStorage.containsFriendship(user1.getId(),user2.getId(),true));
    }

    @Test
    void loadFriends(){
        User user1 = getExpUser1();
        userStorage.add(user1);
        User user2 = getExpUser2();
        userStorage.add(user2);
        userStorage.insertFriendship(user1.getId(),user2.getId());
        assertFalse(user1.getFriends().contains(user2.getId()));
        userStorage.loadFriends(user1);
        assertTrue(user1.getFriends().contains(user2.getId()));
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