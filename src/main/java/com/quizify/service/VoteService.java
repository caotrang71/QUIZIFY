package com.quizify.service;

import com.quizify.model.Vote;
import com.quizify.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class VoteService {
    @Autowired
    private VoteRepository voteRepository;

    public Double getAverageStar(long quizBanksID){
        List<Vote> votes = voteRepository.findByQuizBanksID(quizBanksID);

        double sum = 0;
        for (Vote star : votes){
            sum += star.getStar();
        }
        if (votes.isEmpty()){
            return 0.0;
        }
        double average = (sum / votes.size());
        BigDecimal bd = new BigDecimal(average).setScale(1, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void saveVote(long userID,long quizBanksID, int star){
        Vote newvote =new Vote();

        newvote.setVotedBy(userID);
        newvote.setQuizBanksID(quizBanksID);
        newvote.setStar(star);
        voteRepository.save(newvote);
    }

    public int getStarVoteByUser(long userID,long quizBanksID){
        Vote vote = voteRepository.findByVotedByAndQuizBanksID(userID,quizBanksID);
        return (vote != null) ? vote.getStar(): 0;
    }

    public List<Vote> getAllUserVoted(long quizBanksID){
        List<Vote> votes = voteRepository.findByQuizBanksID(quizBanksID);
        if (votes != null){
            return votes;
        }
        return null;
    }
}
