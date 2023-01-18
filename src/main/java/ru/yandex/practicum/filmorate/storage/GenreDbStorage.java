package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Genre mapToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getInt("GENRE_ID"));
        genre.setName(resultSet.getString("NAME"));
        return genre;
    }

    @Override
    public Genre add(Genre genre) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("GENRES")
                .usingGeneratedKeyColumns("GENRE_ID");

        Map<String, Object> values = new HashMap<>();
        values.put("NAME", genre.getName());

        genre.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return genre;
    }

    @Override
    public Genre replace(Genre genre) {
        String sql = "UPDATE GENRES SET NAME = ? WHERE GENRE_ID = ?";
        jdbcTemplate.update(sql, genre.getName(), genre.getId());
        return genre;
    }

    @Override
    public Genre delete(Genre genre) {
        final String sql = "DELETE FROM GENRES WHERE GENRE_ID = ?";
        jdbcTemplate.update(sql, genre.getId());
        return genre;
    }

    @Override
    public Genre getById(Integer id) {
        String sql = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        List<Genre> result = jdbcTemplate.query(sql, this::mapToGenre, id);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public Collection<Genre> getValues() {
        String sql = "SELECT * FROM GENRES ORDER BY GENRE_ID";
        return jdbcTemplate.query(sql, this::mapToGenre);
    }
}