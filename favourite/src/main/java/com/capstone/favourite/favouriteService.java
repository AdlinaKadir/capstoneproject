package com.capstone.favourite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class favouriteService {
    @Autowired
    private favouriteRepo repo;

    public Favourite saveFav(Favourite favourite)
    {
        return repo.save(favourite);
    }
}
