package com.example.demo.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String otp;
    private LocalDateTime expriTime;
    private int attempts;
    public OTP() {}
    public OTP(int id,String email, String otp, LocalDateTime expriTime, int attempts) {
        this.id = id;
        this.email = email;
        this.otp = otp;
        this.expriTime = expriTime;
        this.attempts = attempts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getExpriTime() {
        return expriTime;
    }

    public void setExpriTime(LocalDateTime expriTime) {
        this.expriTime = expriTime;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    @Override
    public String toString() {
        return "OTP{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", otp='" + otp + '\'' +
                ", expriTime=" + expriTime +
                ", attempts=" + attempts +
                '}';
    }
}
