package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.memory.interfaces.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component("InMemoryUserStorage")
public class InMemoryUserStorage extends AbstractInMemoryStorage<User> implements UserStorage {

    final Map<Integer, User> storage = new HashMap<>();
    private int id = 1;

    @Override
    public User add(User user) {
        user.setId(id);
        storage.put(id, user);
        id++;
        return user;
    }

    @Override
    public User replace(User user) {
        storage.replace(user.getId(), user);
        return user;
    }

    @Override
    public User delete(User user) {
        storage.remove(user.getId());
        return user;
    }

    @Override
    public User getById(Integer id) {
        return storage.get(id);
    }

    @Override
    public Collection<User> getValues() {
        return storage.values()
                .stream()
                .sorted(this::compare)
                .collect(Collectors.toList());
    }

    private int compare(User u0, User u1) {
        return Integer.compare(u0.getId(), u1.getId());
    }
}
