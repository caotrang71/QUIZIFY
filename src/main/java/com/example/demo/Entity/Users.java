package com.example.demo.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;
    private String fullname;
    private String birthdate;
    private int gender;
    @Column(unique = true)
    private String email;
    private String username;
    private String password;
    private LocalDateTime created_at;
    private LocalDateTime modified_at;
    private int status;
    private int role_id;
    @PrePersist
    protected void onCreate() {
        created_at = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        modified_at = LocalDateTime.now();
    }

    public Users() {
    }

    public Users(int user_id, String fullname, String birthdate, int gender, String email, String username, String password, LocalDateTime created_at, LocalDateTime modified_at, int status, int role_id) {
        this.user_id = user_id;
        this.fullname = fullname;
        this.birthdate = birthdate;
        this.gender = gender;
        this.email = email;
        this.username = username;
        this.password = password;
        this.created_at = created_at;
        this.modified_at = modified_at;
        this.status = status;
        this.role_id = role_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getModified_at() {
        return modified_at;
    }

    public void setModified_at(LocalDateTime modified_at) {
        this.modified_at = modified_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    @Override
    public String toString() {
        return "Users{" +"user_id=" + user_id +
                ", fullname='" + fullname + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", gender=" + gender +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", created_at=" + created_at +
                ", modified_at=" + modified_at +
                ", status=" + status +
                ", role_id=" + role_id +
                '}';
    }
}