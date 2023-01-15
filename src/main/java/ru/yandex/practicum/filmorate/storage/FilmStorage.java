package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Film add(Film film);

    Film replace(Film film);

    Film delete(Film film);

    Film getById(int id);

    Collection<Integer> getIds();

    Collection<Film> getValues();

    Film addUserLike(int filmId, int userId);

    Film deleteUserLike(int filmId, int userId);

    Collection<Film> getTopRatedFilms(int size);
}
