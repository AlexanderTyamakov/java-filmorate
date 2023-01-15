package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User add(User user);

    User replace(User user);

    User delete(User user);

    User getById(int id);

    Collection<Integer> getIds();

    Collection<User> getValues();

    User addUserFriend(int userId1, int userId2);

    User deleteUserFriend(int userId1, int userId2);

    Collection<User> getFriendOfUser(int userId1);

    Collection<User> getCommonFriends(int userId1, int userId2);
}
