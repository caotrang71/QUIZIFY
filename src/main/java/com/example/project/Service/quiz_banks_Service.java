package com.example.project.Service;

import com.example.project.Repository.quiz_bank_Repository;
import com.example.project.entity.quiz_banks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class quiz_banks_Service {
    @Autowired
    private quiz_bank_Repository quizBankRepository;
    public List<quiz_banks> getAllListQuizBanks(){
        return quizBankRepository.findAll();
    }
    public void deleteQuizBanks(int id){
        quiz_banks quizBanks = quizBankRepository.findById(id).orElse(null);
        if (quizBanks != null){
            quizBankRepository.deleteById(id);
        }
    }
}
