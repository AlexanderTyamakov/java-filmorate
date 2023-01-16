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
    public Collection<User> getValues() {
        return users.values()
                .stream()
                .sorted(this::compare)
                .collect(Collectors.toList());
    }

    private int compare(User u0, User u1) {
        return Integer.compare(u0.getId(), u1.getId());
    }
}
