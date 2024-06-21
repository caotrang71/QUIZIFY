package com.quizify.repository;

import com.quizify.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTP,Integer> {
    Optional<OTP> findFirstByEmail(String email);
}
