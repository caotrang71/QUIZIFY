package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int role_id;
    private String type_of_role;

    public roles(){}

    public roles(int role_id, String type_of_role) {
        this.role_id = role_id;
        this.type_of_role = type_of_role;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public String getType_of_role() {
        return type_of_role;
    }

    public void setType_of_role(String type_of_role) {
        this.type_of_role = type_of_role;
    }

    @Override
    public String toString() {
        return "roles{" +
                "role_id=" + role_id +
                ", type_of_role='" + type_of_role + '\'' +
                '}';
    }
}
