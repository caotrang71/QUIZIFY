package com.quizify.repository;

import com.quizify.model.Question;
import com.quizify.model.Test;
import com.quizify.model.TestHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestHistoryRepository extends JpaRepository<TestHistory, Integer> {
    TestHistory getTestHistoryById(long id);
    Optional<TestHistory> findByQuestionAndTest(Question question, Test test);
    List<TestHistory> getTestHistoriesByTest(Test test);
    List<TestHistory> findByTest(Test test);
}
