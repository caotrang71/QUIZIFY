package com.quizify.service;

import com.quizify.model.Question;
import com.quizify.model.QuizBank;
import com.quizify.model.User;
import com.quizify.repository.QuestionRepository;
import com.quizify.repository.QuizBankRepository;
import com.quizify.repository.UserRepository;
import org.springframework.beans.factory.annotation.*;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuizBankService {

    @Autowired
    private QuizBankRepository quizBankRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public List<QuizBank> getUserCreatedQuizBanks(User user) {
        return quizBankRepository.findByCreatedBy(user);
    }

    public List<QuizBank> getPublicQuizBanks(User user) {
        return quizBankRepository.findByEditableTrueAndCreatedByNot(user);
    }

    public QuizBank createQuizBank(QuizBank quizBank) {
        if (quizBank.getQuestions() == null || quizBank.getQuestions().size() < 2) {
            throw new IllegalArgumentException("A quiz bank must contain at least 2 questions.");
        }
        // Save the quiz bank and its questions
        QuizBank savedQuizBank = quizBankRepository.save(quizBank);
        for (Question question : quizBank.getQuestions()) {
            question.setQuizBank(savedQuizBank);
            questionRepository.save(question);
        }
        return savedQuizBank;
    }

    public void deleteQuizBank(Long id) {
        quizBankRepository.deleteById(id);
    }

    // Other service methods
    public void save(QuizBank quizBank){
        quizBankRepository.save(quizBank);
    }
    public List<QuizBank> getAllQuizBank() {
        return quizBankRepository.findAll();
    }
    public QuizBank getQuizBankById(long id) {
        return quizBankRepository.getReferenceById(id);
    }
}

