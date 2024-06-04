package com.quizify.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "quiz_banks_id", nullable = false)
    private QuizBank quizBank;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<QuestionChoice> questionChoices;

}