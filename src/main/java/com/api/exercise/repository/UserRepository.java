package com.api.exercise.repository;

import com.api.exercise.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {


    User findByEmail(String email);
}
