package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FriendshipDbStorage implements FriendshipStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
