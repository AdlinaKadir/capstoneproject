package com.collabera.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ratingService {
    @Autowired
    private ratingRepo repo;

    public List<Rating> getratingbyuserId(int id)
    {
        return repo.findByUserId(id);
    }

    public List<Rating> getRatings()
    {
        return repo.findAll();
    }

    public Rating getUserIdAndMovieId(int userId, int movieId)
    {
        return repo.findByUserIdAndMovieId(userId,movieId);
    }

    public List<Rating> getlistbymovieId(int id)
    {
        return repo.findByMovieId(id);
    }

    public List<double[]> findAllDataFromrating(int id) {
        return repo.findlistmovierating(id);
    }

    public Rating saveRating(Rating rating)
    {
        return repo.save(rating);
    }


}
