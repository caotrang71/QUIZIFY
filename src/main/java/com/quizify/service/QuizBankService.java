package com.quizify.service;

import com.quizify.model.*;
import com.quizify.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizBankService {

    @Autowired
    private QuizBankRepository quizBankRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionChoiceRepository questionChoiceRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    public List<QuizBank> getAllQuizBanks() {
        return quizBankRepository.findAll();
    }

    @Transactional
    public QuizBank createQuizBank(QuizBank quizBank) {
        // Create and save the new QuizBank
        QuizBank newQuizBank = new QuizBank();
        newQuizBank.setBankName(quizBank.getBankName());
        newQuizBank.setDescription(quizBank.getDescription());
        newQuizBank.setStatus(true);
        newQuizBank.setCreatedAt(LocalDateTime.now());
        newQuizBank.setModifiedAt(LocalDateTime.now());
        newQuizBank.setCreatedBy(userRepository.getReferenceById(3L));
        newQuizBank.setSubcategory(quizBank.getSubcategory());// Ensure this user ID exists in your database
            quizBankRepository.save(newQuizBank);


        for (Question question : quizBank.getQuestions()) {
            question.setQuizBank(newQuizBank);
            question = questionRepository.save(question);

            for (QuestionChoice choice : question.getQuestionChoices()) {

                choice.setQuestion(question);
                questionChoiceRepository.save(choice);
            }
        }

        return newQuizBank;
    }


    public QuizBank getQuizBankById(long id) {
        Optional <QuizBank> optional = quizBankRepository.findById(id);
        QuizBank quizBank = null;
        if (optional.isPresent()) {
            quizBank = optional.get();
        } else {
            throw new RuntimeException(" Quiz Bank not found for id :: " + id);
        }
        return quizBank;
    }

@Transactional
public QuizBank updateQuizBank(QuizBank quizBank) {
    QuizBank existingQuizBank = quizBankRepository.findById(quizBank.getId())
            .orElseThrow(() -> new RuntimeException("QuizBank not found"));

    existingQuizBank.setBankName(quizBank.getBankName());
    existingQuizBank.setDescription(quizBank.getDescription());
    existingQuizBank.setSubcategory(quizBank.getSubcategory());
    existingQuizBank.setModifiedAt(LocalDateTime.now());

    existingQuizBank = quizBankRepository.save(existingQuizBank);
    List<Question> existingQuestions = questionRepository.getQuestionsByQuizBank(existingQuizBank);
    for (Question existingQuestion : existingQuestions) {

        boolean found = false;
        for (Question updatedQuestion : quizBank.getQuestions()) {
            if (existingQuestion.getId().equals(updatedQuestion.getId())) {
                found = true;
                break;
            }
        }

        if (!found) {
            List<QuestionChoice> existingChoices = questionChoiceRepository.getQuestionChoiceByQuestion(existingQuestion);
            for (QuestionChoice choice : existingChoices) {
                questionChoiceRepository.delete(choice);
            }
            questionRepository.delete(existingQuestion);
        }
    }


    for (Question question : quizBank.getQuestions()) {
        question.setQuizBank(existingQuizBank);

        if (question.getId() == null) {

            questionRepository.save(question);
        } else {

            Question existingQuestion = questionRepository.findById(question.getId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));
            existingQuestion.setContent(question.getContent());

            questionRepository.save(existingQuestion);
        }

        for (QuestionChoice choice : question.getQuestionChoices()) {

            choice.setQuestion(question);


            if (choice.getId() == null) {

                questionChoiceRepository.save(choice);
            } else {

                QuestionChoice existingChoice = questionChoiceRepository.findById(choice.getId())
                        .orElseThrow(() -> new RuntimeException("QuestionChoice not found"));
                existingChoice.setContent(choice.getContent());
                existingChoice.setCorrectOrNot(choice.getCorrectOrNot());

                questionChoiceRepository.save(existingChoice);
            }
        }
    }

    return existingQuizBank;
}


    public QuizBank saveQuizBank(QuizBank quizBank) {
        quizBank.setCreatedAt(LocalDateTime.now());
        quizBankRepository.save(quizBank);


        for (Question question : quizBank.getQuestions()) {
            question.setQuizBank(quizBank);
            question = questionRepository.save(question);

            for (QuestionChoice choice : question.getQuestionChoices()) {
                choice.setQuestion(question);
                questionChoiceRepository.save(choice);
            }
        }
        return quizBank;
    }


    public void deleteQuizBankById(Long id) {
        this.quizBankRepository.deleteById(id);
    }



    public List<QuizBank> searchQuizBank(String keyword) {

        System.out.println(keyword);
        String trimmedKeyword = keyword.trim().replaceAll("\\s+", "");

        List<QuizBank> filteredQuizBanks = new ArrayList<>();

        for (QuizBank quizBank : getAllQuizBanks()) {

            String bankName = quizBank.getBankName().toLowerCase().replaceAll("\\s+", "");

            if (bankName.contains(trimmedKeyword.toLowerCase())
                        ) {
                filteredQuizBanks.add(quizBank);
            }
        }
        System.out.println(filteredQuizBanks);

        return filteredQuizBanks;
    }

}

