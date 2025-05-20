package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Film> add(Film film) {
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
        int id = keyHolder.getKey().intValue();
        if (!CollectionUtils.isEmpty(film.getGenres())) {
            Set<Genre> genres = new LinkedHashSet<>(film.getGenres());
            jdbcTemplate.batchUpdate(sqlGenre,
                    genres,
                    100,
                    (PreparedStatement ps, Genre genre) -> {
                        ps.setInt(1, id);
                        ps.setInt(2, genre.getId());
                    });
        }
        return get(id);
    }

    @Override
    public Optional<Film> update(Film film) {
        String sqlQuery = "update films set " +
                "name = ?, description = ?, releaseDate = ?, duration = ?, ratings_id= ? where id = ?";
        try {
            jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                    film.getDuration(), film.getMpa().getId(), film.getId());
            String sqlQueryDeleteGenres = "delete from film_genres where film_id = ?";
            jdbcTemplate.update(sqlQueryDeleteGenres, film.getId());
            if (!CollectionUtils.isEmpty(film.getGenres())) {
                String sqlGenre = "insert into film_genres(film_id, genre_id) values(?, ?)";
                Set<Genre> genres = new LinkedHashSet<>(film.getGenres());
                jdbcTemplate.batchUpdate(sqlGenre,
                        genres,
                        100,
                        (PreparedStatement ps, Genre genre) -> {
                            ps.setInt(1, film.getId());
                            ps.setInt(2, genre.getId());
                        });
            }
            return get(film.getId());
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Film> getAll() {
        String sql = "select f.id, f.name, f.description, f.releaseDate, f.duration, f.ratings_id, ratings.name as " +
                "namempa from films as f inner join ratings on f.ratings_id=ratings.id";
        List<Film> films = jdbcTemplate.query(sql, this::makeFilm);

        Map<Integer, Film> filmById = films.stream()
                .collect(Collectors.toMap(Film::getId, Function.identity()));


        String genresQuery = "select * from film_genres join genre on film_genres.genre_id=genre.id";
        jdbcTemplate.query(genresQuery, rs -> {
            int filmId = rs.getInt("film_id");
            Film film = filmById.get(filmId);
            if (film != null) {
                Genre genre = new Genre(rs.getInt("id"), rs.getString("name"));
                film.addGenre(genre);
            }
        });

        String likesQuery = "select * from likes_films";
        jdbcTemplate.query(likesQuery, rs -> {
            int filmId = rs.getInt("films_id");
            Film film = filmById.get(filmId);
            if (film != null) {
                film.addLike(rs.getInt("users_id"));
            }
        });
        return new ArrayList<>(filmById.values());
    }

    @Override
    public Optional<Film> get(int id) {
        try {
            String sql = "select f.id, f.name, f.description, f.releaseDate, f.duration, f.ratings_id, ratings.name as " +
                    "namempa from films as f inner join ratings on f.ratings_id=ratings.id where f.id=?";
            Film film = jdbcTemplate.queryForObject(sql, this::makeFilm, id);
            String genresQuery = "select * from film_genres join genre on film_genres.genre_id=genre.id" +
                    " where film_genres.film_id= ? order by film_genres.genre_id";
            jdbcTemplate.query(genresQuery, rs -> {
                Genre genre = new Genre(rs.getInt("id"), rs.getString("name"));
                film.addGenre(genre);
            }, id);

            String likesQuery = "select * from likes_films where films_id=?";
            jdbcTemplate.query(likesQuery, rs -> {
                film.addLike(rs.getInt("users_id"));
            }, id);
            return Optional.of(film);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
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
            throw new NotFoundException("Лайк для фильма id=" + filmId + " не найден");
        }
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sqlQuery = "select f.id, f.name, f.description, f.releaseDate, f.duration, f.ratings_id, ratings.name AS namempa, " +
                "count(lf.users_id) from films as f inner join ratings on f.ratings_id=ratings.id " +
                "left join LIKES_FILMS lf on lf.films_id=f.id group by f.id order by count(lf.users_id) DESC LIMIT ?";
        return new ArrayList<>((jdbcTemplate.query(sqlQuery, this::makeFilm, count)));
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("releaseDate").toLocalDate();
        int duration = rs.getInt("duration");
        int ratingId = rs.getInt("ratings_id");
        String nameMpa = rs.getString("namempa");
        Mpa mpa = new Mpa(ratingId, nameMpa);
        return new Film(id, name, description, releaseDate, duration, new HashSet<>(), new ArrayList<>(), mpa);
    }
}
