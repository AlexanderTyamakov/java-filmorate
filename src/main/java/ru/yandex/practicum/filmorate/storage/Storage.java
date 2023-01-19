package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface Storage<T> {

    T add(T obj);

    T replace(T obj);

    T delete(T obj);

    T getById(Integer id);

    Collection<T> getValues();
}
