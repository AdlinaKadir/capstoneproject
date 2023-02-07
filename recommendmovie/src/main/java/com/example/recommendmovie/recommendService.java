package com.example.recommendmovie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class recommendService {

    @Autowired
    private recommendRepo repo;

    public List<Recommend> findAll(int id)
    {
        return repo.findAll(id);
    }
}
