package com.example.recommendmovie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class recommendRepo {
    @Autowired
    JdbcTemplate jdbcTemplate;
    private RowMapper<Recommend> rowMapper = (ResultSet rs, int rowNum) -> {
        Recommend matrix = new Recommend();
        matrix.setMovieId(rs.getInt(1));

        return matrix;
    };

    public List<Recommend> findAll(int id) {
        String column = "ID_" + id;
        return jdbcTemplate.query("SELECT MOVIE_ID FROM MATRIX WHERE " + column
                + " > 0.5 AND MOVIE_ID <> " + id + " ORDER BY " + column
                + " DESC FETCH FIRST 5 ROWS ONLY", rowMapper);
    }
}
