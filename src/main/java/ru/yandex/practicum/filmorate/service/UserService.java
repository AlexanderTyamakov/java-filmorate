package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private UserStorage userStorage;
    private FriendshipStorage friendshipStorage;

    @Autowired
    public UserService(UserStorage userStorage, FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }


    public User create(User user) {
        validate(user);
        userStorage.add(user);
        log.info("Пользователь добавлен в коллекцию");
        return user;
    }


    public User update(User user) {
        validate(user);
        if (getIds().contains(user.getId())) {
            userStorage.replace(user);
            log.info("Пользователь обновлен в коллекции");
        } else {
            log.error("Пользователь в коллекции не найден");
            throw new UserNotFoundException("Ошибка при обновлении: пользователь c id = " + user.getId() + " не найден");
        }
        return user;
    }

    public Collection<User> findAll() {
        log.info("Возвращен список пользователей");
        Collection<User> users = userStorage.getValues();
        users.forEach(friendshipStorage::loadFriends);
        return users;
    }

    public User getById (Integer userId) {
        if (!getIds().contains(userId)) {
            log.error("Пользователь в коллекции не найден");
            throw new UserNotFoundException("Ошибка при поиске: пользователь id = " + userId + " не найден");
        }
        User user = userStorage.getById(userId);
        friendshipStorage.loadFriends(user);
        return user;
    }

    public User addFriend(Integer userId1, Integer userId2) {
        if (getIds().contains(userId1)) {
            if (getIds().contains(userId2)) {
                if (userStorage.getById(userId1).containsFriend(userId2)) {
                    log.error("Пользователь уже есть в друзьях");
                    return userStorage.getById(userId1);
                }
                userStorage.getById(userId1).addFriends(userId2);
                if (friendshipStorage.containsFriendship(userId2, userId1, false)) {
                    friendshipStorage.updateFriendship(userId2, userId1, true, userId2, userId1);
                } else if (!friendshipStorage.containsFriendship(userId1, userId2, null)){
                    friendshipStorage.insertFriendship(userId1, userId2);
                }
                return userStorage.getById(userId1);
            } else {
                log.error("Пользователь в коллекции не найден");
                throw new UserNotFoundException("Ошибка при добавлении в друзья: пользователь c id = " + userId2 + " не найден");
            }
        } else {
            log.error("Пользователь в коллекции не найден");
            throw new UserNotFoundException("Ошибка при добавлении в друзья: пользователь c id = " + userId1 + " не найден");
        }
    }

    public User deleteFriend(Integer userId1, Integer userId2) {
        if (getIds().contains(userId1)) {
            if (getIds().contains(userId2)) {
                userStorage.getById(userId1).deleteFriends(userId2);
                if (friendshipStorage.containsFriendship(userId1, userId2, false)) {
                    friendshipStorage.removeFriendship(userId1, userId2);
                } else if (friendshipStorage.containsFriendship(userId1, userId2, true)) {
                    friendshipStorage.updateFriendship(userId2, userId1, false, userId1, userId2);
                } else if (friendshipStorage.containsFriendship(userId2, userId1, true)) {
                    friendshipStorage.updateFriendship(userId2, userId1, false, userId2, userId1);
                }
                return userStorage.getById(userId1);
            } else {
                log.error("Пользователь в коллекции не найден");
                throw new UserNotFoundException("Ошибка при удалении из друзей: пользователь c id = " + userId2 + " не найден");
            }
        } else {
            log.error("Пользователь в коллекции не найден");
            throw new UserNotFoundException("Ошибка при удалении из друзей: пользователь c id = " + userId1 + " не найден");
        }
    }

    public Collection<User> returnFriendCollection(Integer userId) {
        if (getIds().contains(userId)) {
            Set<Integer> temp = userStorage.getById(userId).getFriends();
            return userStorage.getValues().stream()
                    .filter(x -> temp.contains(x.getId()))
                    .sorted(this::compare)
                    .collect(Collectors.toList());
        } else {
            log.error("Пользователь в коллекции не найден");
            throw new UserNotFoundException("Ошибка при поиске друзей: пользователь c id = " + userId + " не найден");
        }
    }

    public Collection<User> returnCommonFriends(Integer userId1, Integer userId2) {
        if (getIds().contains(userId1)) {
            if (getIds().contains(userId2)) {
                Set<Integer> temp =  userStorage.getById(userId1).getFriends()
                        .stream()
                        .filter(userStorage.getById(userId2).getFriends()::contains)
                        .collect(Collectors.toSet());

                return userStorage.getValues().stream()
                        .filter(x -> temp.contains(x.getId()))
                        .sorted(this::compare)
                        .collect(Collectors.toList());
            } else {
                log.error("Пользователь в коллекции не найден");
                throw new UserNotFoundException("Ошибка при поиске общих друзей: пользователь c id = " + userId2 + " не найден");
            }
        } else {
            log.error("Пользователь в коллекции не найден");
            throw new UserNotFoundException("Ошибка при поиске общих друзей: пользователь c id = " + userId1 + " не найден");
        }
    }

    public void validate(User user) {
        if (user.getLogin().contains(" ")) {
            log.error("В логине пользователя есть пробел");
            throw new ValidationException("Не пройдена валидация пользователя по логину: " + user.getLogin());
        }
        if ((user.getName() == null) || (user.getName().isBlank())) {
            log.info("Пользователю в качестве имени присвоен логин");
            user.setName(user.getLogin());
        }
    }

    private Collection<Integer> getIds() {
        return userStorage.getValues().stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    private int compare(User u0, User u1) {
        return Integer.compare(u0.getId(), u1.getId());
    }
}

