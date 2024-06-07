package com.quizify.service;

import com.quizify.model.Question;
import com.quizify.model.QuizBank;
import com.quizify.model.User;
import com.quizify.repository.QuestionRepository;
import com.quizify.repository.QuizBankRepository;
import com.quizify.repository.SubcategoryRepository;
import com.quizify.repository.UserRepository;
import org.springframework.beans.factory.annotation.*;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuizBankService {

    @Autowired
    private QuizBankRepository quizBankRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    public List<QuizBank> getAllQuizBanks() {
        return quizBankRepository.findAll();
    }

    public void createQuizBank(QuizBank quizBank) {
        // Create a new instance of QuizBank
        QuizBank newQuizBank = new QuizBank();
        // Set the fields based on the passed quizBank object
        newQuizBank.setBankName(quizBank.getBankName());
        newQuizBank.setDescription(quizBank.getDescription());
        newQuizBank.setImage(quizBank.getImage()); // Assuming you want to pass the image, not just an empty string.
        newQuizBank.setEditable(true); // Assuming new quiz banks are always editable initially.

        // Set created_at to the current datetime
        newQuizBank.setCreatedAt(LocalDateTime.now());
        newQuizBank.setModifiedAt(LocalDateTime.now());

        // Set the creator of the quiz bank (assuming the user ID is known and valid)
        newQuizBank.setCreatedBy(userRepository.getReferenceById(3L));

        // Set the subcategory of the quiz bank (assuming the subcategory is part of the passed quizBank object)
//        newQuizBank.getSubcategory().setSubcategoryId(quizBank.getSubcategory().getSubcategoryId(32));
        newQuizBank.setSubcategory(subcategoryRepository.getReferenceById(32));
        // Save the new quiz bank to the repository
        quizBankRepository.save(newQuizBank);
    }


    public QuizBank updateQuizBank(QuizBank quizBank) {
        return quizBankRepository.save(quizBank);
    }

    public QuizBank getQuizBankById(long id) {
        Optional <QuizBank> optional = quizBankRepository.findById(id);
        QuizBank quizBank = new QuizBank();
        if (optional.isPresent()) {
            quizBank = optional.get();
        } else {
            throw new RuntimeException(" Quiz Bank not found for id :: " + id);
        }
        return quizBank;
    }
//
    public void saveQuizBank(QuizBank quizBank) {
        this.quizBankRepository.save(quizBank);
    }
//    public void deleteQuizBank(QuizBank quizBank) {
//        quizBankRepository.delete(quizBank);
//    }
//
//    public List<QuizBank> getUserCreatedQuizBanks(User user) {
//        return quizBankRepository.findByCreatedBy(user);
//    }
//
//    public List<QuizBank> getPublicQuizBanks(User user) {
//        return quizBankRepository.findByEditableTrueAndCreatedByNot(user);
//    }
//
//    public QuizBank createQuizBank(QuizBank quizBank) {
//        if (quizBank.getQuestions() == null || quizBank.getQuestions().size() < 2) {
//            throw new IllegalArgumentException("A quiz bank must contain at least 2 questions.");
//        }
//        // Save the quiz bank and its questions
//        QuizBank savedQuizBank = quizBankRepository.save(quizBank);
//        for (Question question : quizBank.getQuestions()) {
//            question.setQuizBank(savedQuizBank);
//            questionRepository.save(question);
//        }
//        return savedQuizBank;
//    }
//
//    public void deleteQuizBank(Long id) {
//        quizBankRepository.deleteById(id);
//    }
//
//    // Other service methods
//    public void save(QuizBank quizBank){
//        quizBankRepository.save(quizBank);
//    }
//    public List<QuizBank> getAllQuizBank() {
//        return quizBankRepository.findAll();
//    }
//    public QuizBank getQuizBankById(long id) {
//        return quizBankRepository.getReferenceById(id);
//    }
}

