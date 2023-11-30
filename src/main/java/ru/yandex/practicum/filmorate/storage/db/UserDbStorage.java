package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void create(User user) {
        jdbcTemplate.update("insert into users (name, login, email, birthday) values (?, ?, ?, ?)",
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday());
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update("update users set "
                        + "name = ?, login = ?, email = ?, birthday = ? "
                        + "where id = ?",
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
    }

    @Override
    public List<User> getAllData() {
        return jdbcTemplate.query("select * from users", UserDbStorage::createUserFromDb);
    }

    @Override
    public User getById(long id) {
        List<User> usersList = jdbcTemplate.query("select * from users where id = ?",
                UserDbStorage::createUserFromDb, id);

        if (usersList.size() != 1) {
            throw new DataNotFoundException("User id-{} not found");
        }

        return usersList.get(0);
    }

    @Override
    public boolean delete(long id) {
        getById(id);
        jdbcTemplate.update("delete from users where id = ?", id);
        return true;
    }

    private static User createUserFromDb(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }
}
