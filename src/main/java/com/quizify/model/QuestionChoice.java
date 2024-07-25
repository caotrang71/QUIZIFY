package com.quizify.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "question_choices")
public class QuestionChoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "choice_id")
    private Long id;

    @Column(name = "correct_or_not")
    private Boolean correctOrNot;

    @Column(name = "content")
    private String content;


    @Column(name = "image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @OneToMany(mappedBy = "questionChoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestHistory> testHistories;

}
