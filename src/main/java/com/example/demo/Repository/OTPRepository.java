package com.example.demo.Repository;

import com.example.demo.Entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP,Integer> {
    Optional<OTP> findFirstByEmail(String email);
}
