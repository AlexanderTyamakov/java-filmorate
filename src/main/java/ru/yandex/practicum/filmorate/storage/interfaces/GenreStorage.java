package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

public interface GenreStorage extends Storage<Genre> {

    void loadGenre(Film film);

}
