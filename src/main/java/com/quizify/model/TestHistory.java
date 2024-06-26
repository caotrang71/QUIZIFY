package com.quizify.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "test_histories")
public class TestHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @ManyToOne
    @JoinColumn(name = "choice_id", nullable = true)
    private QuestionChoice questionChoice;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

}
