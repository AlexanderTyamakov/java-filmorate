package ru.yandex.practicum.filmorate.storage.database.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.memory.interfaces.FilmStorage;

public interface FilmDStorage extends FilmStorage {

    void saveLikes(Film film);

    void loadLikes(Film film);
}
