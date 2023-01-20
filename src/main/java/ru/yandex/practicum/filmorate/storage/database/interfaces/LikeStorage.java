package ru.yandex.practicum.filmorate.storage.database.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.Storage;

public interface LikeStorage  {

    void saveLikes(Film film);

    void loadLikes(Film film);
}
