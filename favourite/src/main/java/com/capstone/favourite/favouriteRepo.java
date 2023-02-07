package com.capstone.favourite;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface favouriteRepo extends JpaRepository<Favourite,Integer> {
}
