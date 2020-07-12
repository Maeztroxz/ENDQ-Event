package com.example.demo.api.repositories;

import com.example.demo.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmailIgnoreCase(String email);
}
