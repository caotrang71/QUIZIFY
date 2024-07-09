package com.quizify.repository;

import com.quizify.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    Vote findByVotedByAndQuizBanksID(long userID,long quizBanksID);
    List<Vote> findByQuizBanksID(long quizBanksID);
}
