package ru.yandex.practicum.filmorate.storage.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.database.interfaces.RatingStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("RatingDbStorage")
public class RatingDbStorage extends AbstractDbStorage<Rating> implements RatingStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public  RatingDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Rating mapToRating(ResultSet resultSet, int rowNum) throws SQLException {
        Rating rating = new Rating();
        rating.setId(resultSet.getInt("RATING_ID"));
        rating.setName(resultSet.getString("NAME"));
        return rating;
    }

    @Override
    public Rating add(Rating rating) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("RATINGS")
                .usingGeneratedKeyColumns("RATING_ID");

        Map<String, Object> values = new HashMap<>();
        values.put("NAME", rating.getName());

        rating.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return rating;
    }

    @Override
    public Rating replace(Rating rating) {
        String sql = "UPDATE RATINGS SET NAME = ? WHERE RATING_ID = ?";
        jdbcTemplate.update(sql, rating.getName(), rating.getId());
        return rating;
    }

    @Override
    public Rating delete(Rating rating) {
        final String sql = "DELETE FROM RATINGS WHERE RATING_ID = ?";
        jdbcTemplate.update(sql, rating.getId());
        return rating;
    }

    @Override
    public Rating getById(Integer id) {
        String sql = "SELECT * FROM RATINGS WHERE RATING_ID = ?";
        List<Rating> result = jdbcTemplate.query(sql, this::mapToRating, id);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public Collection<Rating> getValues() {
        String sql = "SELECT * FROM RATINGS ORDER BY RATING_ID";
        return jdbcTemplate.query(sql, this::mapToRating);
    }
}