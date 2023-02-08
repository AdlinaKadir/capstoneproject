package com.capstone.favourite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class favouriteService {
    @Autowired
    private favouriteRepo repo;

    public Favourite saveFav(Favourite favourite)
    {
        return repo.save(favourite);
    }

    public List<Favourite> getFavbyId(int id)

    {
        return repo.findByUserId(id);
    }

    public String deleteFav(int id)
    {
        repo.deleteById(id);
        return "Favorites no " + id + " has been removed";
    }
}
