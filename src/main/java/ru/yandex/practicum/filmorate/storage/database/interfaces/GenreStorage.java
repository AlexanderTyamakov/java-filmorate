package ru.yandex.practicum.filmorate.storage.database.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.Storage;

public interface GenreStorage extends Storage<Genre> {

    void loadGenre(Film film);

    void saveGenre(Film film);

    void deleteGenre(Film film);

}
