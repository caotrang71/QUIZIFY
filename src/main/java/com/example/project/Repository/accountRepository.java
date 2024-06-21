package com.example.project.Repository;

import com.example.project.entity.users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface accountRepository extends JpaRepository<users, Integer> {
    List<users> findByFullnameContainingIgnoreCase(String fullname);
    List<users> findByEmail(String email);
}
