package ru.yandex.practicum.filmorate.storage.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.database.interfaces.UserDStorage;

import javax.validation.constraints.Size;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Size
@Component("UserDbStorage")
public class UserDbStorage extends AbstractDbStorage<User> implements UserDStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private User mapToUser(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("USER_ID"));
        user.setEmail(resultSet.getString("EMAIL"));
        user.setLogin(resultSet.getString("LOGIN"));
        user.setName(resultSet.getString("NAME"));
        user.setBirthday(resultSet.getDate("BIRTHDAY").toLocalDate());
        return user;
    }

    @Override
    public User add(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("USER_ID");

        Map<String, Object> values = new HashMap<>();
        values.put("EMAIL", user.getEmail());
        values.put("LOGIN", user.getLogin());
        values.put("NAME", user.getName());
        values.put("BIRTHDAY", user.getBirthday());

        user.setId(simpleJdbcInsert.executeAndReturnKey(values).intValue());
        return user;
    }

    @Override
    public User replace(User user) {
        String sql =
                "UPDATE USERS SET LOGIN = ?, EMAIL = ?, NAME = ?, BIRTHDAY = ?" +
                        "WHERE USER_ID = ?";
        jdbcTemplate.update(sql, user.getLogin(), user.getEmail(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public User delete(User user) {
        final String sql = "DELETE FROM USERS WHERE USER_ID = ?";
        jdbcTemplate.update(sql, user.getId());
        return user;
    }

    @Override
    public User getById(Integer id) {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
        List<User> result = jdbcTemplate.query(sql, this::mapToUser, id);
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public Collection<User> getValues() {
        String sql = "SELECT * FROM USERS ORDER BY USER_ID";
        return jdbcTemplate.query(sql, this::mapToUser);
    }

    @Override
    public void insertFriendship(Integer id, Integer friendId) {
        String sql = "INSERT INTO FRIENDSHIP (USER_ID1, USER_ID2, CONFIRMED) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, id, friendId, false);
    }

    @Override
    public void removeFriendship(Integer filterId1, Integer filterId2) {
        String sql = "DELETE FROM FRIENDSHIP WHERE USER_ID1 = ? AND USER_ID2 = ?";
        jdbcTemplate.update(sql, filterId1, filterId2);
    }

    @Override
    public void updateFriendship(Integer id1, Integer id2, boolean confirmed, Integer filterId1, Integer filterId2) {
        String sql =
                "UPDATE FRIENDSHIP SET USER_ID1 = ?, USER_ID2 = ?, CONFIRMED = ? " +
                        "WHERE USER_ID1 = ? AND USER_ID2 = ?";
        jdbcTemplate.update(sql, id1, id2, confirmed, filterId1, filterId2);
    }

    @Override
    public boolean containsFriendship(Integer filterId1, Integer filterId2, Boolean filterConfirmed) {
        String sql = "SELECT * FROM FRIENDSHIP WHERE USER_ID1 = ? AND USER_ID2 = ? AND  CONFIRMED = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, filterId1, filterId2, filterConfirmed);
        return rows.next();
    }

    @Override
    public void loadFriends(User user) {
        String sql =
                "(SELECT USER_ID2 ID FROM FRIENDSHIP  WHERE USER_ID1 = ?) " +
                        "UNION " +
                        "(SELECT USER_ID1 ID FROM FRIENDSHIP  WHERE USER_ID2 = ? AND  CONFIRMED = true)";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, user.getId(), user.getId());
        while (sqlRowSet.next()) {
            user.addFriends(sqlRowSet.getInt("id"));
        }
    }








}
