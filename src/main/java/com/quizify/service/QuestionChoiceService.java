package com.quizify.service;

import com.quizify.model.Question;
import com.quizify.model.QuestionChoice;
import com.quizify.repository.QuestionChoiceRepository;
import com.quizify.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionChoiceService {
    @Autowired
    private QuestionChoiceRepository questionChoiceRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public List<QuestionChoice> getQuestionChoiceByQuestion(Question question) {
        return questionChoiceRepository.getQuestionChoiceByQuestion(question);
    }

    public QuestionChoice saveQuestionChoice(QuestionChoice questionChoice) {
        return questionChoiceRepository.save(questionChoice);
    }

//    public List<QuestionChoice> getQuestionChoiceByQuestion(Question question) {
//       return questionChoiceRepository.
//    }
}
