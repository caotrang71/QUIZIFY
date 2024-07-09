//package com.quizify.controller;
//
//import com.quizify.repository.VoteRepository;
//import com.quizify.service.VoteService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//@Controller
//public class VoteController {
//    @Autowired
//    private VoteRepository voteRepository;
//    @Autowired
//    private VoteService voteService;
//
//    @PostMapping("/vote")
//    public String voteQuizBanks(@RequestParam("userID") long userID,
//                                @RequestParam("quizBanksID") long quizBanksID,
//                                @RequestParam("star") int star,
//                                RedirectAttributes redirectAttributes)
//    {
//        voteService.saveVote(userID,quizBanksID,star);
//        return "redirect:/quiz-banks/quiz-banks-list";
//    }
//
//}
