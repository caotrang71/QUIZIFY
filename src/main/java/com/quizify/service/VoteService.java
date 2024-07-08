package com.quizify.service;

import com.quizify.model.Vote;
import com.quizify.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return sum / votes.size();
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
