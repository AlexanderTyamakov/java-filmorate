package ru.yandex.practicum.filmorate.storage.database.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public interface FriendshipStorage {

    void insertFriendship(Integer id, Integer friendId);

    void removeFriendship(Integer filterId1, Integer filterId2);

    void updateFriendship(Integer id1, Integer id2, boolean confirmed, Integer filterId1, Integer filterId2);

    boolean containsFriendship(Integer filterId1, Integer filterId2, Boolean filterConfirmed);

    void loadFriends(User user);
}
