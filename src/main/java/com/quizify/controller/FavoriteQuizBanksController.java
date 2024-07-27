package com.quizify.controller;

import com.quizify.model.FavoriteQuizBanks;
import com.quizify.model.QuizBank;
import com.quizify.model.User;
import com.quizify.repository.FavoriteQuizBanksRepository;
import com.quizify.repository.QuizBankRepository;
import com.quizify.service.FavoriteQuizBanksService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class FavoriteQuizBanksController {
    @Autowired
    private FavoriteQuizBanksRepository favoriteQuizBanksRepository;
    @Autowired
    private FavoriteQuizBanksService favoriteQuizBanksService;
    @Autowired
    private QuizBankRepository quizBankRepository;

    @GetMapping("/show/favorite_quiz_banks")
    public String showFavoriteQuizBanks(Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        List<FavoriteQuizBanks> favorList = favoriteQuizBanksRepository.findByUser(user);
        model.addAttribute("favorList", favorList);
        return "favorite_quiz_banks";
    }

    @DeleteMapping("/delete/favorite_quiz_banks/{id}")
    @ResponseBody
    public void deleteFavoriteQuizBanks(@PathVariable long id){
        favoriteQuizBanksService.deleteFavoriteQuizBanks(id);
    }

    @PostMapping("/add/favorite_quiz_bank")
    public String addFavoriteQuizBanks(@RequestParam long quizBankId, HttpSession session,
                                     RedirectAttributes redirectAttributes){
        QuizBank quizBank = quizBankRepository.findById(quizBankId).orElse(null);
        User user = (User) session.getAttribute("user");
        favoriteQuizBanksService.addFavoriteQuizBanks(quizBank,user);
        redirectAttributes.addFlashAttribute("mess", "add favorite quiz bank success");
        return "redirect:/quiz-banks/quiz-bank-detail/"+quizBankId;
    }
}
