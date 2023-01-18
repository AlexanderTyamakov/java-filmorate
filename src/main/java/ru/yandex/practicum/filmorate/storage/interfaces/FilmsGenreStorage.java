package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

public interface FilmsGenreStorage {

    void saveGenre(Film film);

    void loadGenre(Film film);
}
