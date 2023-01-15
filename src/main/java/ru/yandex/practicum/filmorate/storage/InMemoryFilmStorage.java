package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @Override
    public Film add(Film film) {
        film.setId(id);
        film.setUsersLikes(new HashSet<>());
        films.put(id, film);
        id++;
        return film;
    }

    @Override
    public Film replace(Film film) {
        film.setUsersLikes(new HashSet<>());
        films.replace(film.getId(), film);
        return film;
    }

    @Override
    public Film delete(Film film) {
        films.remove(film.getId());
        return film;
    }

    @Override
    public Film getById(int id) {
        return films.get(id);
    }

    @Override
    public Collection<Integer> getIds() {
        return films.keySet();
    }

    @Override
    public Collection<Film> getValues() {
        return films.values()
                .stream()
                .sorted(this::compare)
                .collect(Collectors.toList());
    }

    @Override
    public Film addUserLike(int filmId, int userId) {
        films.get(filmId).addUserLike(userId);
        return films.get(filmId);
    }

    @Override
    public Film deleteUserLike(int filmId, int userId) {
        films.get(filmId).deleteUserLike(userId);
        return films.get(filmId);
    }

    @Override
    public Collection<Film> getTopRatedFilms(int size) {
        return films.values().stream()
                .sorted(this::compare)
                .limit(size)
                .collect(Collectors.toSet());
    }

    private int compare(Film f0, Film f1) {
        return -1 * Integer.compare(f0.getUsersLikes().size(), f1.getUsersLikes().size());
    }
}


