package com.quizify.service;

import com.quizify.model.Test;
import com.quizify.model.TestHistory;
import com.quizify.repository.TestHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestHistoryService {
    @Autowired
    private TestHistoryRepository testHistoryRepository;

    public TestHistory getTestHistoryById(long id) {
        return this.testHistoryRepository.getTestHistoryById(id);
    }

    public List<TestHistory> getTestHistoriesByTest(Test test) {
        return testHistoryRepository.getTestHistoriesByTest(test);
    }
}
