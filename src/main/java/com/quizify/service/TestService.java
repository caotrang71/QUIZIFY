package com.quizify.service;

import com.quizify.model.*;
import com.quizify.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class TestService {
   @Autowired
   private TestRepository testRepository;

   @Autowired
    private TestHistoryRepository testHistoryRepository;

    @Autowired
    private QuizBankRepository quizBankRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionChoiceRepository questionChoiceRepository;

    public Test getTestById(long id) {
        return this.testRepository.getTestById(id);
    }

    public Test createTest(Long userId,Long quizBankId, int numberOfQuestions) throws Exception {
        QuizBank quizBank = quizBankRepository.getQuizBankById(quizBankId);
        if (quizBank == null) {
            throw new Exception("QuizBank not found");
        }
//        QuizBank quizBank = quizBankOpt.get();

        if (numberOfQuestions < 1 || numberOfQuestions > quizBank.getQuestions().size()) {
            throw new Exception("Invalid number of questions");
        }

        List<Question> questions = questionRepository.getQuestionsByQuizBank(quizBank);
        Collections.shuffle(questions);
        List<Question> selectedQuestions = questions.subList(0, numberOfQuestions);

        Test test = new Test();
        test.setCreatedBy(userRepository.findById(userId).orElse(null)); // Assuming User class has a constructor that accepts ID
        test.setQuizBank(quizBank);
        test.setNumberOfQuestions(numberOfQuestions);
        test.setStartedAt(LocalDateTime.now());
        test = testRepository.save(test);

        for (Question question : selectedQuestions) {
            TestHistory testHistory = new TestHistory();
            testHistory.setTest(test);
            testHistory.setQuestion(question);
            testHistoryRepository.save(testHistory);
        }

        return test;
    }


    public boolean isTestSubmitted(Long testId) {
        Test test = getTestById(testId);
        return test.getEndedAt() != null && test.getResult() != 0;
    }

    public Test submitTest(Long testId, List<Long> selectedChoiceIds) throws Exception {
        Optional<Test> testOpt = Optional.ofNullable(testRepository.getTestById(testId));
        if (!testOpt.isPresent()) {
            throw new Exception("Test not found");
        }
        Test test = testOpt.get();
        int correctAnswers = 0;

        List<TestHistory> testHistories = test.getTestHistories();
        for (int i = 0; i < testHistories.size(); i++) {
            TestHistory history = testHistories.get(i);
            Long selectedChoiceId = selectedChoiceIds.get(i);
            QuestionChoice selectedChoice = questionChoiceRepository.findById(selectedChoiceId).orElse(null);

            if (selectedChoice != null && selectedChoice.getCorrectOrNot()) {
                correctAnswers++;
            }


            history.setQuestionChoice(selectedChoice);
            testHistoryRepository.save(history);
        }

        test.setResult(correctAnswers);
        test.setEndedAt(LocalDateTime.now());
        return testRepository.save(test);
    }

    public void markTestInProgress(Long testId) throws Exception {
        Test test = getTestById(testId);
        if (test == null) {
            throw new Exception("Test not found");
        }
        // Ensure the result is null to indicate the test is in-progress
        test.setResult(0);
        test.setEndedAt(null); // Optional: Reset the end time if necessary
        testRepository.save(test);
    }

    public List<Test> getAllTests() {
        return testRepository.findAll();
    }
}
