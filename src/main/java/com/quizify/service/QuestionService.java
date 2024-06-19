package com.quizify.service;

import com.quizify.model.*;
import com.quizify.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizBankRepository quizBankRepository;

    @Autowired
    private QuestionChoiceRepository questionChoiceRepository;

    public Question addQuestion(Question question) {
        return questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }

    public List<Question> getQuestionsByQuizBank(QuizBank quizBank){
        return questionRepository.getQuestionsByQuizBank(quizBank);
    }
    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Question getQuestion(Long id) {
        return questionRepository.findById(id).orElse(null);
    }



//    public List<QuestionChoice> getQuestionChoices(Question question) {
//        return questionRepository.getQuestionChoices(question);
//    }

//    public void loadQuestionChoices(Question question) {
//        List<QuestionChoice> c = this.questionChoiceRepository.getQuestionChoiceByQuestion(question);
//        question.setQuestionChoices(c);
//    }

//    public List<Question> getQuestionsByQuizBankId(long id){
//
//    }
//    public ArrayList<Question> getQuestionByQuizBank(QuizBank quizBank){
//        ArrayList<Question> = questionRepository.findByQuizBank(quizBank);
//
//    }

    // Other service methods
}

