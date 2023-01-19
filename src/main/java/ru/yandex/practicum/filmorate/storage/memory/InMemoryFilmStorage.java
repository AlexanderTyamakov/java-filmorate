package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.memory.interfaces.FilmStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component("InMemoryFilmStorage")
public class InMemoryFilmStorage extends AbstractInMemoryStorage<Film> implements FilmStorage {

    final Map<Integer, Film> storage = new HashMap<>();
    private int id = 1;

    @Override
    public Film add(Film film) {
        film.setId(id);
        storage.put(id, film);
        id++;
        return film;
    }

    @Override
    public Film replace(Film film) {
        storage.replace(film.getId(), film);
        return film;
    }

    @Override
    public Film delete(Film film) {
        storage.remove(film.getId());
        return film;
    }

    @Override
    public Film getById(Integer id) {
        return storage.get(id);
    }

    @Override
    public Collection<Film> getValues() {
        return storage.values()
                .stream()
                .sorted(this::compare)
                .collect(Collectors.toList());
    }

    private int compare(Film f0, Film f1) {
        return Integer.compare(f0.getId(), f1.getId());
    }
}


