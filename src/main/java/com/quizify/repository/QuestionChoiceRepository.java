package com.quizify.repository;

import com.quizify.model.Question;
import com.quizify.model.QuestionChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionChoiceRepository extends JpaRepository<QuestionChoice, Long> {
    List<QuestionChoice> getQuestionChoiceByQuestion(Question question);
}

