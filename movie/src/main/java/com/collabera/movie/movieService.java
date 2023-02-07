package com.collabera.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class movieService {

    @Autowired
    private movieRepo repo;
    public Movie getmoviebyId(int id)
    {
        return repo.findById(id).orElse(null);
    }

    public List<Movie> getMovies()
    {
        return repo.findAll();
    }
    public List<Movie> getMoviesbyId(int id)

    {
        return repo.findByMovieId(id);
    }

    public Movie saveMovie(Movie movie)
    {
        return repo.save(movie);
    }

    public Movie getmoviebytmdbId(int id)

    {
        return repo.findByTmdbId(id);
    }
}
