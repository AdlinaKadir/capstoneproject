package com.example.recommendmovie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class recommendController {
    @Autowired
    private recommendService service;

    @GetMapping("/recommends/{id}")
    public List<Recommend> findallMovies(@PathVariable int id)

    {
        return service.findAll(id);
    }
}
