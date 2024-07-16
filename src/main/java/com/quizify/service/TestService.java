package com.quizify.service;

import com.quizify.model.*;
import com.quizify.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
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

    public Test createTest(Long userId,Long quizBankId, String title, int numberOfQuestions, Integer timeLimit, boolean shuffleQuestions, boolean shuffleChoices) throws Exception {
        QuizBank quizBank = quizBankRepository.getQuizBankById(quizBankId);
        if (quizBank == null) {
            throw new Exception("QuizBank not found");
        }

        if (numberOfQuestions < 1 || numberOfQuestions > quizBank.getQuestions().size()) {
            throw new Exception("Invalid number of questions");
        }

        List<Question> questions = questionRepository.getQuestionsByQuizBank(quizBank);
        List<Question> selectedQuestions;
        if (shuffleQuestions) {
            Collections.shuffle(questions);
            selectedQuestions = questions.subList(0, numberOfQuestions);
        } else {
            selectedQuestions = questions.subList(0, numberOfQuestions);
            Collections.shuffle(selectedQuestions);
        }


        Test test = new Test();
        test.setCreatedBy(userRepository.getUserById(userId)); // Assuming User class has a constructor that accepts ID
        test.setQuizBank(quizBank);
        test.setTitle(title);
        test.setNumberOfQuestions(numberOfQuestions);
        test.setStartedAt(LocalDateTime.now());
        test = testRepository.save(test);

        if (timeLimit != null) {
            test.setTimeLimit((int) timeLimit);
        }
        System.out.println("shuffleChoices: "+ shuffleChoices);
        try {
            for (Question question : selectedQuestions) {
                List<QuestionChoice> choices = new ArrayList<>(question.getQuestionChoices());
                if (shuffleChoices) {
                    Collections.shuffle(choices);
                    System.out.println("Shuffled Choices for Question " + question.getId() + ": " + choices.stream().map(QuestionChoice::getId).collect(Collectors.toList()));
                }

                TestHistory testHistory = new TestHistory();
                testHistory.setTest(test);
                testHistory.setQuestion(question);
                testHistory.setShuffledChoices(choices);
                testHistoryRepository.save(testHistory);

                List<QuestionChoice> answers = testHistory.getShuffledChoices();
                System.out.println("Shuffled Choices for Question " + testHistory.getQuestion().getId() + ": " + answers.stream().map(QuestionChoice::getId).collect(Collectors.toList()));
            }
        } catch (Exception e) {
            System.err.println("Error creating test history: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Error creating test history", e);
        }
        System.out.println("Time Limit: " + test.getTimeLimit());

        List<TestHistory> history = testHistoryRepository.getTestHistoriesByTest(test);
        System.out.print("Thu tu dap an: ");
        for(TestHistory historyItem : history) {
            for(QuestionChoice questionChoice : historyItem.getQuestion().getQuestionChoices()) {
                System.out.println(questionChoice.getId());
            }

        }
        return test;
    }


    public boolean isTestSubmitted(Long testId) {
        Test test = getTestById(testId);
        return test.getEndedAt() != null && test.getResult() != 0;
    }

//    public Test submitTest(Long testId, List<Long> selectedChoiceIds) throws Exception {
//        Optional<Test> testOpt = Optional.ofNullable(testRepository.getTestById(testId));
//        if (!testOpt.isPresent()) {
//            throw new Exception("Test not found");
//        }
//        Test test = testOpt.get();
//        int correctAnswers = 0;
//
//        List<TestHistory> testHistories = test.getTestHistories();
//        for (int i = 0; i < testHistories.size(); i++) {
//            TestHistory history = testHistories.get(i);
//            Long selectedChoiceId = selectedChoiceIds.get(i);
//            QuestionChoice selectedChoice = questionChoiceRepository.findById(selectedChoiceId).orElse(null);
//
//            if (selectedChoice != null && selectedChoice.getCorrectOrNot()) {
//                correctAnswers++;
//            }
//
//
//            history.setQuestionChoice(selectedChoice);
//            testHistoryRepository.save(history);
//        }
//
//        test.setResult(correctAnswers);
//        test.setEndedAt(LocalDateTime.now());
//        return testRepository.save(test);
//    }

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
            Long selectedChoiceId = i < selectedChoiceIds.size() ? selectedChoiceIds.get(i) : null;
            QuestionChoice selectedChoice = selectedChoiceId != null ? questionChoiceRepository.findById(selectedChoiceId).orElse(null) : null;

            if (selectedChoice != null && selectedChoice.getCorrectOrNot()) {
                correctAnswers++;
            }

            history.setQuestionChoice(selectedChoice);
            testHistoryRepository.save(history);
        }

        test.setResult(correctAnswers);
        test.setEndedAt(LocalDateTime.now());

        if (test.getTimeLimit() != null) {
            long timeTaken = Duration.between(test.getStartedAt(), test.getEndedAt()).getSeconds();
            test.setTimeTaken((int) timeTaken);
        }

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
