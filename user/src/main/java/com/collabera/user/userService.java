package com.collabera.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class userService {

    @Autowired
    private userRepo repo;

    public User register(User user)
    {
        return repo.save(user);
    }

    public User getproductbyname(String name)
    {
        return repo.findByName(name);
    }

    public List<User> getUsers()
    {
        return repo.findAll();
    }
}
