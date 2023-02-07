package com.collabera.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ratingRepo extends JpaRepository<Rating,Integer> {

    Rating findByUserIdAndMovieId(int userId, int movieId);

    List<Rating> findByUserId(int id);

    @Query("SELECT movierating FROM Rating")
    List<double[]> findlistmovierating(int id);

//    List<Rating> findByMovieIdIn(List<Integer> Id);

    List<Rating> findByMovieId(int id);


//    List<Rating> findByMovieId(int id);
}
