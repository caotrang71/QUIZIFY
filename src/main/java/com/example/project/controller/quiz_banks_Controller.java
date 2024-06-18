package com.example.project.controller;

import com.example.project.Repository.quiz_bank_Repository;
import com.example.project.Service.quiz_banks_Service;
import com.example.project.entity.quiz_banks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Controller
@RequestMapping("/show")
public class quiz_banks_Controller {
    @Autowired
    private quiz_bank_Repository quizBankRepository;
    @Autowired
    private quiz_banks_Service quizBanksService;


    @GetMapping("/quiz_banks")
    public String showAllQuizBanks(@RequestParam(value = "sort", required = false) String sort,Model model){
        if ("nameAsc".equals(sort)) {
            model.addAttribute("ListQuiz", quizBanksService.getQuizBanksByNameAsc());
        } else if ("nameDesc".equals(sort)) {
            model.addAttribute("ListQuiz", quizBanksService.getQuizBanksByNameDesc());
        } else if ("createdAsc".equals(sort)) {
            model.addAttribute("ListQuiz", quizBanksService.getQuizBanksByCreatedAsc());
        } else if ("createdDesc".equals(sort)) {
            model.addAttribute("ListQuiz", quizBanksService.getQuizBanksByCreatedDesc());
        }else {
        List<quiz_banks> ListQuizBanks = quizBanksService.getAllListQuizBanks();
        model.addAttribute("ListQuiz", ListQuizBanks);
        }
        model.addAttribute("currentSort", sort);
        return "quiz_banks_page";
    }
    @DeleteMapping("/deleteQuiz_banks/{id}")
    @ResponseBody
    public void DeleteQuiz_Banks(@PathVariable int id){
        quizBanksService.deleteQuizBanks(id);
    }

    @GetMapping("/search/quizBanks")
    public String searchQuizBanks(@RequestParam("key") String name, Model model){

        List<quiz_banks> listQuizBanksByname = quizBankRepository.findByBankNameContainingIgnoreCase(name);

        if (!listQuizBanksByname.isEmpty()){
            model.addAttribute("searchQuiz" , listQuizBanksByname );
        }
        return "quiz_banks_page";
    }
    

}
