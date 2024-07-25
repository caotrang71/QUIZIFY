package com.quizify.service;

import com.quizify.model.Question;
import com.quizify.model.QuestionChoice;
import com.quizify.model.Test;
import com.quizify.model.TestHistory;
import com.quizify.repository.QuestionChoiceRepository;
import com.quizify.repository.QuestionRepository;
import com.quizify.repository.TestHistoryRepository;
import com.quizify.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestHistoryService {
    @Autowired
    private TestHistoryRepository testHistoryRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TestRepository testRepository;

    @Autowired
    private QuestionChoiceRepository questionChoiceRepository;

    public TestHistory getTestHistoryById(long id) {
        return this.testHistoryRepository.getTestHistoryById(id);
    }

    public List<TestHistory> getTestHistoriesByTest(Test test) {
        return testHistoryRepository.getTestHistoriesByTest(test);
    }
    public void updateTestHistory(Long testId, Long questionId, Long choiceId) {
        Test test = testRepository.findById(Math.toIntExact(testId)).orElseThrow(() -> new RuntimeException("Test not found"));
        Question question = questionRepository.findById(questionId).orElseThrow(() -> new RuntimeException("Question not found"));
        QuestionChoice choice = questionChoiceRepository.findById(choiceId).orElse(null);

        TestHistory testHistory = testHistoryRepository.findByQuestionAndTest(question, test)
                .orElse(new TestHistory());
        testHistory.setTest(test);
        testHistory.setQuestion(question);
        testHistory.setQuestionChoice(choice);
        testHistoryRepository.save(testHistory);
    }
}
