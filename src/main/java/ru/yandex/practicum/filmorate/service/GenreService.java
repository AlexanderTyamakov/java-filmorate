package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.database.interfaces.GenreStorage;

import java.util.Collection;
import java.util.stream.Collectors;
@Slf4j
@Service
public class GenreService {

    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Collection<Genre> getGenres() {
        return genreStorage.getValues();
    }

    public Genre getGenreById(Integer id) {
        if (!getIds().contains(id)) {
            log.error("Рейтинг в коллекции не найден");
            throw new GenreNotFoundException("Ошибка при поиске: жанр id = " + id + " не найден");
        }
        else {
            return genreStorage.getById(id);
        }
    }

    private Collection<Integer> getIds() {
        return genreStorage.getValues().stream()
                .map(Genre::getId)
                .collect(Collectors.toList());
    }
}
