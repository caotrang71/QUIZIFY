package com.quizify.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tests")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "time_limit")
    private Integer timeLimit;

    @Column(name = "time_taken")
    private Long timeTaken;

    @Column(name = "result")
    private int result;

    @Column(name = "number_of_questions")
    private int numberOfQuestions;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "quiz_banks_id", nullable = false)
    private QuizBank quizBank;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestHistory> testHistories;

    public String getFormattedTimeTaken() {
        if (timeTaken == null) return "Not Available";
        long hours = timeTaken / 3600000;
        long minutes = ((timeTaken/1000) % 3600) / 60;
        long seconds = (timeTaken/1000) % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
