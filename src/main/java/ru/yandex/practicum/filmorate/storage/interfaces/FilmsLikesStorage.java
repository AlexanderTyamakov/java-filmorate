package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmsLikesStorage {

    void saveLikes(Film film);

    void loadLikes(Film film);
}
