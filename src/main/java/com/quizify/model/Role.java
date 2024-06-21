package com.quizify.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "role_id")
    private int id;

    @Column(name = "type_of_role")
    private String typeOfRole;
}
