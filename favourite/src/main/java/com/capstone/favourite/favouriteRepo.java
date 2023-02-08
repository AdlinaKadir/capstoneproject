package com.capstone.favourite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface favouriteRepo extends JpaRepository<Favourite,Integer> {
    List<Favourite> findByUserId(int id);
}
