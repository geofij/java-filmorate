package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void create(Film film) {
        String sqlQuery = "insert into films (name, description, release_date, duration, mpa_id) "
                + "values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId());

        this.updateGenres(film);
    }

    @Override
    public void update(Film film) {
        String sqlQuery = "update films set "
                + "name = ?, description = ?, release_date = ?,  duration = ?, mpa_id = ? "
                + "where id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
                );

        this.updateGenres(film);
    }

    @Override
    public List<Film> getAllData() {
        String sqlQuery = "select f.id as film_id, "
                + "f.name as film_name, "
                + "f.description as description, "
                + "f.duration as duration, "
                + "f.release_date as release_date, "
                + "f.mpa_id as mpa_id, "
                + "m.name as mpa_name"
                + " from films as f join mpa as m on f.mpa_id = m.id";
        return jdbcTemplate.query(sqlQuery, this::createFilmFromDb);
    }

    @Override
    public Film getById(long id) {
        String sqlQuery = "select f.id as film_id, "
                + "f.name as film_name, "
                + "f.description as description, "
                + "f.duration as duration, "
                + "f.release_date as release_date, "
                + "f.mpa_id as mpa_id, "
                + "m.name as mpa_name"
                + " from films as f join mpa as m on f.mpa_id = m.id where f.id = ?";
        List<Film> filmsList = jdbcTemplate.query(sqlQuery, this::createFilmFromDb, id);

        if (filmsList.size() != 1) {
            throw new DataNotFoundException("Film id-{} not found");
        }

        return filmsList.get(0);
    }

    private void updateGenres(Film film) {
        if (film.getGenres() != null) {
            String sqlQuery = "delete from film_genres where film_id = ?";
            jdbcTemplate.update(sqlQuery, film.getId());

            String newSqlQuery = "merge into film_genres (film_id, genre_id) values (?, ?)";
            film.getGenres().forEach(genre -> jdbcTemplate.update(newSqlQuery, film.getId(), genre.getId()));
        } else {
            String newSqlQuery = "delete from film_genres where film_id = ?";
            jdbcTemplate.update(newSqlQuery, film.getId());
        }
    }

    private Film createFilmFromDb(ResultSet rs, int rowNum) throws SQLException {
        Mpa filmMpa = Mpa.builder()
                .id(rs.getLong("mpa_id"))
                .name(rs.getString("mpa_name"))
                .build();

        Film film = Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("description"))
                .duration(rs.getInt("duration"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .mpa(filmMpa)
                .rate(0)
                .build();

        String sqlQuery = "select count(*) as rate from likes where film_id = ?";
        Integer rate = jdbcTemplate.queryForObject(sqlQuery, Integer.class, rs.getLong("id"));

        if (rate != null) {
            film.setRate(rate);
        }

        sqlQuery = "select * from film_genres as fg join genres as g on fg.genre_id = g.id where film_id = ?";
        List<Genre> filmGenres = jdbcTemplate.query(sqlQuery,
                (res, rn) -> GenreDbStorage.createGenre(res, rn, "genre_id"), rs.getLong("id"));

        film.setGenres(new HashSet<>(filmGenres));

        return film;
    }
}
