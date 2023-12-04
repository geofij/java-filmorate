package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendsDbStorage implements FriendsStorage {
    private final JdbcTemplate jdbcTemplate;

    private static User createUserFromDb(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("friend_id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }

    @Override
    public void addFriend(long userId, long friendId) {
        jdbcTemplate.update("merge into friends (user_id, friend_id) values (?, ?)", userId, friendId);
    }

    @Override
    public boolean deleteFriend(long userId, long friendId) {
        return jdbcTemplate.update("delete from friends where user_id = ? and friend_id = ?", userId, friendId) > 0;
    }

    @Override
    public List<User> getUserFriends(long userId) {
        return jdbcTemplate.query("select * from friends as f join users as u on f.friend_id = u.id  "
                + "where user_id = ?", FriendsDbStorage::createUserFromDb, userId);
    }
}
