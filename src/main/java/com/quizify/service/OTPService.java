package com.quizify.service;

import com.quizify.model.OTP;
import com.quizify.repository.OTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OTPService {
    @Autowired
    private OTPRepository otpRepository;

    public String generateOTP() {
        Random ran = new Random();
        int otp = 100000 + ran.nextInt(900000);
        return String.valueOf(otp);
    }
    public OTP createOTP(String email) {
        OTP otp = new OTP();
        otp.setEmail(email);
        otp.setOtp(generateOTP());
        otp.setExpriTime(LocalDateTime.now().plusMinutes(1));
        otp.setAttempts(0);
        return otpRepository.save(otp);
    }

    public Optional<OTP> getObjectOTP(String email) {
        return otpRepository.findTopByEmailOrderByExpriTimeDesc(email);
    }
    public void deleteOTP(String email) {
        Optional<OTP> otp = otpRepository.findTopByEmailOrderByExpriTimeDesc(email);
        if (otp.isPresent()) {
            otpRepository.delete(otp.get());
        }
    }
    public boolean isValidOTP(String email, String otp) {
        Optional<OTP> otpEntity = otpRepository.findTopByEmailOrderByExpriTimeDesc(email);
        if (otpEntity == null) {
            return false;
        }

        if (otpEntity.get().getAttempts() >= 3 || Duration.between(otpEntity.get().getExpriTime(), LocalDateTime.now()).toSeconds() >= 60) {
            otpRepository.delete(otpEntity.get());
            return false;
        }

        if (!otpEntity.get().getOtp().equals(otp)) {
            otpEntity.get().setAttempts(otpEntity.get().getAttempts() + 1);
            otpRepository.save(otpEntity.get());
            return false;
        }

        return true;
    }
}
