package com.collabera.movie;

import com.collabera.movie.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface movieRepo extends JpaRepository<Movie,Integer> {

    Movie findByTmdbId(int id);


    List<Movie> findByMovieId(int id);
}
