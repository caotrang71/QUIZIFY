package com.quizify.repository;

import com.quizify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<User, Long> {
    List<User> findByFullNameContainingIgnoreCase(String fullName);
    List<User> findByEmail(String email);
}
