package ru.yandex.practicum.filmorate.storage;

import jdk.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Repository
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getGenres() {
        String sqlQuery = "select * from genre";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeGenre(rs, rowNum));
    }

    public Genre getGenreForId(int id) {

        try{
            String sqlQuery = "select * from genre where id=?";
            return jdbcTemplate.queryForObject(sqlQuery, (rs, rowNum) -> makeGenre(rs, rowNum), id);
        }catch (RuntimeException e){
            throw new ObjectNotFoundException("Жанр id= " + id + " не найден");
        }
    }

    public  Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(name);
        return genre;
    }
}
