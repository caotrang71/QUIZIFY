package com.quizify.repository;

import com.quizify.model.QuizBank;
import com.quizify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizBankRepository extends JpaRepository<QuizBank, Long> {

    List<QuizBank> findByCreatedBy(User user);
    List<QuizBank> findByEditableTrueAndCreatedByNot(User user);
}

