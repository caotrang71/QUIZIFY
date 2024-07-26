package com.quizify.service;

import com.quizify.model.FavoriteQuizBanks;
import com.quizify.model.QuizBank;
import com.quizify.model.User;
import com.quizify.repository.FavoriteQuizBanksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteQuizBanksService {
    @Autowired
    private FavoriteQuizBanksRepository favoriteQuizBanksRepository;

    public void deleteFavoriteQuizBanks(long id){
        FavoriteQuizBanks favoriteQuizBanks = favoriteQuizBanksRepository.findById(id).orElse(null);
        if (favoriteQuizBanks != null){
            favoriteQuizBanksRepository.delete(favoriteQuizBanks);
        }
    }

    public void addFavoriteQuizBanks(QuizBank quizBank, User user){
        FavoriteQuizBanks favoriteQuizBanks = new FavoriteQuizBanks();
        favoriteQuizBanks.setQuizBank(quizBank);
        favoriteQuizBanks.setUser(user);
        favoriteQuizBanksRepository.save(favoriteQuizBanks);
    }
}
