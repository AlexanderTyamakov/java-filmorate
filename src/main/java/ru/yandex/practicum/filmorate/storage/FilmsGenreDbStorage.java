package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmsGenreStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmsLikesStorage;

import java.util.Set;

@Component
@Slf4j
public class FilmsGenreDbStorage implements FilmsGenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmsGenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void saveGenre(Film film) {
        String sql = "INSERT INTO FILMS_GENRES (FILM_ID, GENRE_ID) VALUES(?, ?)";
        Set<Integer> genres = film.getGenres();
        if (genres == null) {
            return;
        }
        for (Integer genre : genres) {
            jdbcTemplate.update(sql, film.getId(), genre);
        }
    }

    @Override
    public void loadGenre(Film film) {
        String sql = "SELECT GENRE_ID FROM FILMS_GENRES WHERE FILM_ID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, film.getId());
        while (sqlRowSet.next()) {
            film.addGenre(sqlRowSet.getInt("USER_ID"));
        }
    }
}
