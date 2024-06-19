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
        Optional<QuizBank> quizBankOpt = quizBankRepository.findById(quizBankId);
        if (!quizBankOpt.isPresent()) {
            throw new Exception("QuizBank not found");
        }
        QuizBank quizBank = quizBankOpt.get();

        if (numberOfQuestions < 1 || numberOfQuestions > quizBank.getQuestions().size()) {
            throw new Exception("Invalid number of questions");
        }

        List<Question> questions = quizBank.getQuestions();
        Collections.shuffle(questions);
        List<Question> selectedQuestions = questions.subList(0, numberOfQuestions);

        Test test = new Test();
        test.setCreatedBy(userRepository.getReferenceById(userId)); // Assuming User class has a constructor that accepts ID
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

    public Test submitTest(Long testId, List<Long> selectedChoiceIds) throws Exception {
        Optional<Test> testOpt = Optional.ofNullable(testRepository.getTestById(testId));
        if (!testOpt.isPresent()) {
            throw new Exception("Test not found");
        }
        Test test = testOpt.get();
        int correctAnswers = 0;

        for (TestHistory history : test.getTestHistories()) {
            QuestionChoice selectedChoice = questionChoiceRepository.findById(history.getQuestionChoice().getId()).orElse(null);
            if (selectedChoice != null && selectedChoice.getCorrectOrNot()) {
                correctAnswers++;
            }
        }

        test.setResult(correctAnswers);
        test.setEndedAt(LocalDateTime.now());
        return testRepository.save(test);
    }

    public List<Test> getAllTests() {
        return testRepository.findAll();
    }
}
