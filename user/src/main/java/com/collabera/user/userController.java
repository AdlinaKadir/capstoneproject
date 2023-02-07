package com.collabera.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class userController {

    @Autowired
    private userService service;
    @PostMapping("/register")
    public User register(@RequestBody User user)
    {
        return service.register(user);
    }

    @GetMapping("/findbyname/{name}")
    public User findproductbyName(@PathVariable String name)
    {
        return service.getproductbyname(name);
    }

    @GetMapping("/users")
    public List<User> findallUsers()
    {
        return service.getUsers();
    }
}
