package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public User add(User user) {
        user.setId(id);
        user.setFriends(new HashSet<>());
        users.put(id, user);
        id++;
        return user;
    }

    @Override
    public User replace(User user) {
        user.setFriends(new HashSet<>());
        users.replace(user.getId(), user);
        return user;
    }

    @Override
    public User delete(User user) {
        users.remove(user.getId());
        return user;
    }

    @Override
    public User getById(int id) {
        return users.get(id);
    }

    @Override
    public Collection<Integer> getIds() {
        return users.keySet();
    }

    @Override
    public Collection<User> getValues() {
        return users.values()
                .stream()
                .sorted(this::compare)
                .collect(Collectors.toList());
    }

    @Override
    public User addUserFriend(int userId1, int userId2) {
        users.get(userId1).addFriends(userId2);
        users.get(userId2).addFriends(userId1);
        return users.get(userId1);
    }

    @Override
    public User deleteUserFriend(int userId1, int userId2) {
        users.get(userId1).deleteFriends(userId2);
        users.get(userId2).deleteFriends(userId1);
        return users.get(userId1);
    }

    @Override
    public Collection<User> getFriendOfUser(int userId1) {
        Set<Integer> temp = users.get(userId1).getFriends();
        return users.values().stream()
                .filter(x -> temp.contains(x.getId()))
                .sorted(this::compare)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<User> getCommonFriends(int userId1, int userId2) {
        Set<Integer> temp =  users.get(userId1).getFriends()
                .stream()
                .filter(users.get(userId2).getFriends()::contains)
                .collect(Collectors.toSet());

        return users.values().stream()
                .filter(x -> temp.contains(x.getId()))
                .sorted(this::compare)
                .collect(Collectors.toList());
    }

    private int compare(User u0, User u1) {
        return Integer.compare(u0.getId(), u1.getId());
    }
}
