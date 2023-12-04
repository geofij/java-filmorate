package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.HashSet;
import java.util.stream.Collectors;

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
        this.updateDirectors(film);
    }

    @Override
    public Film update(Film film) {
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
        this.updateDirectors(film);

        return getById(film.getId());
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

    @Override
    public List<Film> getSortedFilmsByDirector(String sortType, long directorId) {
        List<Film> films = new ArrayList<>();

        if (sortType.equals("likes")) {
            films = jdbcTemplate.query("select fd.film_id as film_id, "
                                    + "f.name as film_name, "
                                    + "f.description as description, "
                                    + "f.duration as duration, "
                                    + "f.release_date as release_date, "
                                    + "f.mpa_id as mpa_id, "
                                    + "m.name as mpa_name "
                                    + "from films as f join mpa as m on f.mpa_id = m.id "
                                    + "join film_director as fd on f.id = fd.film_id "
                                    + "where fd.director_id = ?",
                            this::createFilmFromDb, directorId).stream()
                    .sorted(Comparator.comparing(Film::getRate, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        }

        if (sortType.equals("year")) {
            films = jdbcTemplate.query("select fd.film_id as film_id, "
                    + "f.name as film_name, "
                    + "f.description as description, "
                    + "f.duration as duration, "
                    + "f.release_date as release_date, "
                    + "f.mpa_id as mpa_id, "
                    + "m.name as mpa_name "
                    + "from films as f join mpa as m on f.mpa_id = m.id "
                    + "join film_director as fd on f.id = fd.film_id "
                    + "where fd.director_id = ? "
                    + "order by release_date", this::createFilmFromDb, directorId);
        }

        return films;
    }

    private void updateGenres(Film film) {
        jdbcTemplate.update("delete from film_genres where film_id = ?", film.getId());

        if (film.getGenres() != null) {
            film.getGenres().forEach(genre -> jdbcTemplate.update("merge into film_genres (film_id, genre_id) "
                    + "values (?, ?)", film.getId(), genre.getId()));
        }
    }

    private void updateDirectors(Film film) {
        jdbcTemplate.update("delete from film_director where film_id = ?", film.getId());

        if (film.getDirectors() != null) {
            film.getDirectors().forEach(director -> jdbcTemplate.update("merge into film_director "
                    + "(film_id, director_id) "
                    + "values (?, ?)", film.getId(), director.getId()));
        }
    }


    @Override
    public boolean delete(long id) {
        getById(id);
        jdbcTemplate.update("delete from films where id = ?", id);
        return true;
    }

    @Override
    public List<Film> findByTitle(String query) {
        return jdbcTemplate.query("select f.id as film_id, " +
                        "f.name as film_name, " +
                        "f.description as description, " +
                        "f.duration as duration, " +
                        "f.release_date as release_date, " +
                        "f.mpa_id as mpa_id, " +
                        "m.name as mpa_name " +
                        "from films as f join mpa as m on f.mpa_id = m.id " +
                        "where f.name ilike ?", this::createFilmFromDb,"%" + query + "%");
    }

    @Override
    public List<Film> findByDirector(String query) {
        return jdbcTemplate.query("select f.id as film_id, " +
                "f.name as film_name, " +
                "f.description as description, " +
                "f.duration as duration, " +
                "f.release_date as release_date, " +
                "f.mpa_id as mpa_id, " +
                "m.name as mpa_name " +
                "from films as f " +
                "join mpa as m on f.mpa_id = m.id " +
                "join film_director as fd on fd.film_id = f.id " +
                "join director as d on d.id = fd.director_id " +
                "where d.name ilike ?", this::createFilmFromDb, "%" + query + "%");
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
                + "from likes where film_id = ?", Integer.class, rs.getLong("film_id"));

        if (rate != null) {
            film.setRate(rate);
        }

        List<Genre> filmGenres = jdbcTemplate.query("select * from film_genres as fg " +
                        "join genres as g on fg.genre_id = g.id where film_id = ?",
                (res, rn) -> GenreDbStorage.createGenre(res, "genre_id"), rs.getLong("film_id"));

        film.setGenres(new HashSet<>(filmGenres));

        List<Director> filmDirectors = jdbcTemplate.query("select * from film_director as fd " +
                "join director as d on fd.director_id = d.id where film_id = ?",
                DirectorDbStorage::createDirector,
                rs.getLong("film_id"));

        film.setDirectors(new HashSet<>(filmDirectors));

        return film;
    }
}
