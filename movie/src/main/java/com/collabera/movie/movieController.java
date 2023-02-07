package com.collabera.movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class movieController {

    @Autowired
    private movieService service;
    @GetMapping("/findbyid/{id}")
    public Movie findmoviebyId(@PathVariable int id)

    {
        return service.getmoviebyId(id);
    }

    @GetMapping("/movies")
    public List<Movie> findallMovies()
    {
        return service.getMovies();
    }

    @GetMapping("/movies/{id}")
    public List<Movie> findallMoviesbyId(@PathVariable int id)
    {
        return service.getMoviesbyId(id);
    }

    @PostMapping("/addMovie")
    public Movie addMovie(@RequestBody Movie movie)
    {
        return service.saveMovie(movie);
    }

    @GetMapping("/findbytmdbid/{id}")
    public Movie findmoviebytmdbId(@PathVariable int id)

    {
        return service.getmoviebytmdbId(id);
    }
}
