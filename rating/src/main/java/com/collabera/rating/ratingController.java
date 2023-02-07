package com.collabera.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.Math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class ratingController {

    @Autowired
    private ratingService service;
    @GetMapping("/findbyuserId/{id}")
    public List<Rating> findratingbyUserId(@PathVariable int id)

    {
        return service.getratingbyuserId(id);
    }

    @GetMapping("/ratings")
    public List<Rating> findallRatings()
    {
        return service.getRatings();
    }

    @GetMapping("/findbyuseridandmovieid/{userId}/{movieId}")
    public Rating findratingbyuserIdandmovieId(@PathVariable("userId") int userId, @PathVariable("movieId") int movieId)
    {
        return service.getUserIdAndMovieId(userId,movieId);
    }

    @GetMapping("/findbymovieid")
    public List<Rating> findbymovieId(@PathVariable int movieId)
    {
        return service.getlistbymovieId(movieId);
    }

    @GetMapping("/allrating")
    public List<double[]> findallRating(@PathVariable int id)
    {
        return service.findAllDataFromrating(id);
    }

    @PostMapping("/addrating")
    public Rating addRating(@RequestBody Rating rating)
    {
        return service.saveRating(rating);
    }
}
