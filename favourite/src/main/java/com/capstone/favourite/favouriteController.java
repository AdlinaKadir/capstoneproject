package com.capstone.favourite;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class favouriteController {
    @Autowired
    private favouriteService service;

    @PostMapping("/addFav")
    public Favourite addFav(@RequestBody Favourite favourite)
    {
        return service.saveFav(favourite);
    }
}
