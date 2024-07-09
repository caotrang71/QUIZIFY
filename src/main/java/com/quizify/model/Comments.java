package com.quizify.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String comment;

    private LocalDateTime create_at;

    private long comment_by;

    @Column(name = "quiz_banks_id")
    private long quizBanksID;

    @Column(name = "full_name")
    private String fullName;

    @PrePersist
    protected void onCreate() {
        create_at = LocalDateTime.now();
    }
}
