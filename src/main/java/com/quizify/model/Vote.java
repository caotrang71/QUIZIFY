package com.quizify.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Persistent;

import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "vote_for_quiz_banks")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "create_id")
    private int createID;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "star")
    private int star;

    @Column(name = "voted_by")
    private long votedBy;

    @Column(name = "quiz_banks_id")
    private long  quizBanksID;

    @PrePersist
    protected void onCreated(){
        createdAt = LocalDateTime.now();
    }
}
