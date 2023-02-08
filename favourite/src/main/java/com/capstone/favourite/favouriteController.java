package com.capstone.favourite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class favouriteController {
    @Autowired
    private favouriteService service;

    @PostMapping("/addFav")
    public Favourite addFav(@RequestBody Favourite favourite)
    {
        return service.saveFav(favourite);
    }

    @GetMapping("/favs/{id}")
    public List<Favourite> findallFavouritebyId(@PathVariable int id)
    {
        return service.getFavbyId(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteFav(@PathVariable int id)
    {
        return service.deleteFav(id);
    }
}
