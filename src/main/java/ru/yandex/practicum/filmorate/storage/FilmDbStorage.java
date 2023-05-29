package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.controller.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@Primary
public class FilmDbStorage implements FilmStorage {
    private LocalDate minDate = LocalDate.of(1895, 12, 28);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "select * from films";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs, rowNum));
    }

    @Override
    public Film addFilm(Film film) {
        validate(film);
        String sqlQuery = "insert into films(name, description, releaseDate, duration, ratings_id) values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        String sqlGenre = "insert into film_genres(film_id, genre_id) values(?, ?)";
        if(film.getGenres()!=null){
            Set<Genre> genres = film.getGenres();
            for (Genre genre : genres) {
                jdbcTemplate.update(sqlGenre, keyHolder.getKey().intValue(), genre.getId());
            }
        }
        film.setId(keyHolder.getKey().intValue());
        film.setMpa(setMpa(film.getMpa().getId()));
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        validate(film);
        String sqlQuery = "update films set " +
                "name = ?, description = ?, releaseDate = ?, duration = ?, ratings_id= ? where id = ?";
        try {
            jdbcTemplate.update(sqlQuery
                    , film.getName()
                    , film.getDescription()
                    , film.getReleaseDate()
                    , film.getDuration()
                    , film.getMpa().getId()
                    , film.getId());

            String sqlQueryDeleteGenres = "delete from film_genres where film_id = ?";
            jdbcTemplate.update(sqlQueryDeleteGenres, film.getId());
            if (!film.getGenres().isEmpty()) {
                String sqlQueryGenre = "insert into film_genres(film_id, genre_id) values(?, ?)";
                List<Genre> genres = new ArrayList<>(film.getGenres());
                for (Genre genre : genres) {
                    jdbcTemplate.update(sqlQueryGenre, film.getId(), genre.getId());
                }
            }
            return getFilmForId(film.getId());
        } catch (RuntimeException e) {
            throw new ObjectNotFoundException("Фильм id=" + film.getId() + " не найден");
        }
    }

    @Override
    public Film getFilmForId(int id) {
        try {
            String sql = "select * from films where id=?";
            Film film = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeFilm(rs, rowNum), id);
            return film;
        } catch (RuntimeException e) {
            throw new ObjectNotFoundException("фильм id=" + id + " не найден");
        }
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sqlQuery = "insert into likes_films(films_id, users_id) values (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String sqlQuery = "delete from likes_films where films_id = ? and users_id = ?";
        boolean flag = jdbcTemplate.update(sqlQuery, filmId, userId) > 0;
        if (!flag) {
            throw new ObjectNotFoundException("Ошибка в id");
        }
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("releaseDate").toLocalDate();
        int duration = rs.getInt("duration");
        int ratingId = rs.getInt("ratings_id");
        Film film = new Film(name, description, releaseDate, duration);
        film.setMpa(setMpa(ratingId));
        film.setId(id);
        film.addLikes(setLikes(id));
        film.addGenres(setGenre(id));
        return film;
    }

    private List<Genre> setGenre(int id) {
        String sql = "select genre.id, genre.name from film_genres left join genre " +
                "on film_genres.genre_id = genre.id " +
                "where film_genres.film_id = ?";
        List<Genre> genres = new ArrayList<>(jdbcTemplate.query(sql, (this::makeGenre), id));
        return genres;
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(name);
        return genre;
    }

    private Mpa setMpa(int ratingId) {
        String sql = "select * from ratings where id=?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeMpa(rs, rowNum), ratingId);
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        Mpa mpa = new Mpa();
        mpa.setName(name);
        mpa.setId(id);
        return mpa;
    }

    private List<Integer> setLikes(int filmId) {
        String sqlQuery = "select users_id from likes_films where films_id = ?";
        List<Integer> likes = jdbcTemplate.queryForList(sqlQuery, Integer.class, filmId);
        return likes;
    }

    private void validate(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription() == null || film.getDescription().length() > 200 || film.getDescription().isBlank()) {
            throw new ValidationException("Описание больше 200 символов!");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(minDate)) {
            throw new ValidationException("Дата не может быть раньше 28.12.1895");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность не может быть отрицательной");
        }
    }
}