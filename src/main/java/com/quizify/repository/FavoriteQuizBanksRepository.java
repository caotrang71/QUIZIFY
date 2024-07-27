package com.quizify.repository;

import com.quizify.model.FavoriteQuizBanks;
import com.quizify.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteQuizBanksRepository extends JpaRepository<FavoriteQuizBanks, Long> {
    List<FavoriteQuizBanks> findByUser(User user);
    boolean existsByQuizBank_Id(Long quizBank_id);
}
