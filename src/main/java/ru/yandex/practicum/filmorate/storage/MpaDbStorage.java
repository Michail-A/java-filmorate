package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.exceptions.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> getMpa(){
        String sql = "select * from ratings";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs, rowNum));
    }

    public Mpa getMpaForId(int id){
        try{
            String sql = "select * from ratings where id = ?";
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> makeMpa(rs, rowNum), id);
        }catch (RuntimeException e){
            throw new ObjectNotFoundException("Рейтинга с id=" + id + " нет");
        }
    }
    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        Mpa mpa = new Mpa();
        mpa.setId(id);
        mpa.setName(name);
        return mpa;
    }
}
