package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.HashSet;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void create(Film film) {
        jdbcTemplate.update("insert into films (name, description, release_date, duration, mpa_id) "
                        + "values (?, ?, ?, ?, ?)", film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId());

        this.updateGenres(film);
    }

    @Override
    public void update(Film film) {
        jdbcTemplate.update("update films set "
                        + "name = ?, description = ?, release_date = ?,  duration = ?, mpa_id = ? "
                        + "where id = ?",
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
        return jdbcTemplate.query("select f.id as film_id, "
                + "f.name as film_name, "
                + "f.description as description, "
                + "f.duration as duration, "
                + "f.release_date as release_date, "
                + "f.mpa_id as mpa_id, "
                + "m.name as mpa_name"
                + " from films as f join mpa as m on f.mpa_id = m.id", this::createFilmFromDb);
    }

    @Override
    public Film getById(long id) {
        List<Film> filmsList = jdbcTemplate.query("select f.id as film_id, "
                + "f.name as film_name, "
                + "f.description as description, "
                + "f.duration as duration, "
                + "f.release_date as release_date, "
                + "f.mpa_id as mpa_id, "
                + "m.name as mpa_name"
                + " from films as f join mpa as m on f.mpa_id = m.id where f.id = ?", this::createFilmFromDb, id);

        if (filmsList.size() != 1) {
            throw new DataNotFoundException("Film id-{} not found");
        }

        return filmsList.get(0);
    }

    private void updateGenres(Film film) {
        if (film.getGenres() != null) {
            jdbcTemplate.update("delete from film_genres where film_id = ?", film.getId());

            film.getGenres().forEach(genre -> jdbcTemplate.update("merge into film_genres (film_id, genre_id) "
                    + "values (?, ?)", film.getId(), genre.getId()));
        } else {
            jdbcTemplate.update("delete from film_genres where film_id = ?", film.getId());
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

        Integer rate = jdbcTemplate.queryForObject("select count(*) as rate "
                + "from likes where film_id = ?", Integer.class, rs.getLong("id"));

        if (rate != null) {
            film.setRate(rate);
        }

        List<Genre> filmGenres = jdbcTemplate.query("select * from film_genres as fg " +
                        "join genres as g on fg.genre_id = g.id where film_id = ?",
                (res, rn) -> GenreDbStorage.createGenre(res, "genre_id"), rs.getLong("id"));

        film.setGenres(new HashSet<>(filmGenres));

        return film;
    }
}
