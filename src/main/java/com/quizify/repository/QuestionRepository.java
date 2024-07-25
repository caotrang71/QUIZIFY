package com.quizify.repository;

import com.quizify.model.Question;
import com.quizify.model.QuestionChoice;
import com.quizify.model.QuizBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> getQuestionsByQuizBank(QuizBank quizBank);
//    List<QuestionChoice> getQuestionChoices(Question question);
//    void loadQuestionChoices(Question question);
}

