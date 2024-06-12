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

//    public List<QuizBank> getQuizBanksListWithQuestions(){
//        List<QuizBank> quizBanks = quizBankRepository.findAll();
//        for (QuizBank q : quizBanks) {
//            loadQuestions(q);
//            for (Question question : q.getQuestions()) {
//                questionRepository.loadQuestionChoices(question);
//            }
//        }return quizBanks;
//    }
    @Transactional
    public QuizBank createQuizBank(QuizBank quizBank) {
        // Create and save the new QuizBank
        QuizBank newQuizBank = new QuizBank();
        newQuizBank.setBankName(quizBank.getBankName());
        newQuizBank.setDescription(quizBank.getDescription());
        newQuizBank.setEditable(true);
        newQuizBank.setCreatedAt(LocalDateTime.now());
        newQuizBank.setModifiedAt(LocalDateTime.now());
        newQuizBank.setCreatedBy(userRepository.getReferenceById(3L)); // Ensure this user ID exists in your database
        newQuizBank.setSubcategory(subcategoryRepository.getReferenceById(32)); // Ensure this subcategory ID exists in your database
        quizBankRepository.save(newQuizBank);

        // Save each question and its choices
        for (Question question : quizBank.getQuestions()) {
            question.setQuizBank(newQuizBank); // Set the newQuizBank to the question
            question = questionRepository.save(question);

            for (QuestionChoice choice : question.getQuestionChoices()) {

                // Set the correctOrNot attribute based on the selected radio button value
                System.out.println("Content: "+choice.getContent());

                System.out.println("Correct? "+choice.getCorrectOrNot());
                choice.setQuestion(question); // Set the saved question to the choice
                questionChoiceRepository.save(choice);
            }
        }
        return newQuizBank;
    }



//    public QuizBank updateQuizBank(QuizBank quizBank) {
//        return quizBankRepository.save(quizBank);
//    }

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

//    public void loadQuestions(QuizBank quizBank) {
//        List<Question> questions = questionRepository.getQuestionsByQuizBank(quizBank);
//        quizBank.setQuestions(questions);
//    }
@Transactional
public QuizBank updateQuizBank(QuizBank quizBank) {
    // Fetch the existing quiz bank from the database
    QuizBank existingQuizBank = quizBankRepository.findById(quizBank.getId())
            .orElseThrow(() -> new RuntimeException("QuizBank not found"));

    // Update quiz bank details
    existingQuizBank.setBankName(quizBank.getBankName());
    existingQuizBank.setDescription(quizBank.getDescription());

    // Save or update the quiz bank
    existingQuizBank = quizBankRepository.save(existingQuizBank);

    // Get the existing questions for this quiz bank
    List<Question> existingQuestions = questionRepository.getQuestionsByQuizBank(existingQuizBank);

    // Iterate through the existing questions
    for (Question existingQuestion : existingQuestions) {
        // Check if the existing question is still present in the updated quiz bank
        boolean found = false;
        for (Question updatedQuestion : quizBank.getQuestions()) {
            if (existingQuestion.getId().equals(updatedQuestion.getId())) {
                found = true;
                break;
            }
        }

        // If the existing question is not found in the updated list, delete it along with its choices
        if (!found) {
            List<QuestionChoice> existingChoices = questionChoiceRepository.getQuestionChoiceByQuestion(existingQuestion);
            for (QuestionChoice choice : existingChoices) {
                questionChoiceRepository.delete(choice);
            }
            questionRepository.delete(existingQuestion);
        }
    }

    // Iterate through the questions in the updated quiz bank
    for (Question question : quizBank.getQuestions()) {
        // Set the relationship between question and quiz bank
        question.setQuizBank(existingQuizBank);

        // Save or update the question
        if (question.getId() == null) {
            // If the question is new, save it
            questionRepository.save(question);
        } else {
            // If the question already exists, update its details
            Question existingQuestion = questionRepository.findById(question.getId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));
            existingQuestion.setContent(question.getContent());
            // Save the updated question
            questionRepository.save(existingQuestion);
        }

        // Iterate through the question choices
        for (QuestionChoice choice : question.getQuestionChoices()) {
            // Set the relationship between choice and question
            choice.setQuestion(question);

            // Save or update the question choice
            if (choice.getId() == null) {
                // If the choice is new, save it
                questionChoiceRepository.save(choice);
            } else {
                // If the choice already exists, update its details
                QuestionChoice existingChoice = questionChoiceRepository.findById(choice.getId())
                        .orElseThrow(() -> new RuntimeException("QuestionChoice not found"));
                existingChoice.setContent(choice.getContent());
                existingChoice.setCorrectOrNot(choice.getCorrectOrNot());
                // Save the updated choice
                questionChoiceRepository.save(existingChoice);
            }
        }
    }

    return existingQuizBank;
}






    public QuizBank saveQuizBank(QuizBank quizBank) {
        quizBank.setCreatedAt(LocalDateTime.now());
        quizBankRepository.save(quizBank);

        // Save each question and its choices
        for (Question question : quizBank.getQuestions()) {
            question.setQuizBank(quizBank); // Set the newQuizBank to the question
            question = questionRepository.save(question);

            for (QuestionChoice choice : question.getQuestionChoices()) {
                choice.setQuestion(question); // Set the saved question to the choice
                questionChoiceRepository.save(choice);
            }
        }
        return quizBank;
    }

//    @Transactional
//    public void deleteQuizBank(Long quizBankId) {
//        QuizBank quizBank = quizBankRepository.findById(quizBankId)
//                .orElseThrow(() -> new RuntimeException("QuizBank not found"));
//
//        quizBankRepository.delete(quizBank);
//    }


    public void deleteQuizBankById(Long id) {
        this.quizBankRepository.deleteById(id);
    }



//    public Subcategory getSubcategoryByQuizBank(QuizBank quizBank) {
//        return
//    }

//    public List<Question> getQuestions(QuizBank quizBank) {
//        return this.quizBankRepository.getQuestions(quizBank);
//    }

    public Page<QuizBank> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection){
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending():
        Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo -1, pageSize);
        return this.quizBankRepository.findAll(pageable);
    }

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

