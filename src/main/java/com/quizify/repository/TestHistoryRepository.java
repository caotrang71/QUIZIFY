package com.quizify.repository;

import com.quizify.model.Test;
import com.quizify.model.TestHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestHistoryRepository extends JpaRepository<TestHistory, Integer> {
    TestHistory getTestHistoryById(long id);

    List<TestHistory> getTestHistoriesByTest(Test test);
}
