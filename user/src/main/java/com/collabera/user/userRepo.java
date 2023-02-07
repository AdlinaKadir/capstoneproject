package com.collabera.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepo extends JpaRepository<User,Integer> {
    User findByName(String name);
}
