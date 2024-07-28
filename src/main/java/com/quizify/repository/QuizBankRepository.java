package com.quizify.repository;

import com.quizify.model.Question;
import com.quizify.model.QuizBank;
import com.quizify.model.Subcategory;
import com.quizify.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface QuizBankRepository extends JpaRepository<QuizBank, Long> {
    List<QuizBank> findBySubcategoryId(long subCategoryId);

    QuizBank getQuizBankById(long id);
    void deleteQuizBanksById(long id);

    List<QuizBank> getQuizBanksBySubcategory(Subcategory subcategory);
    List<QuizBank> getQuizBanksByCreatedBy(User user);

    List<QuizBank> findByBankNameContainingIgnoreCase(String bankName);
    List<QuizBank> findAllByOrderByBankNameAsc();
    List<QuizBank> findAllByOrderByBankNameDesc();
    List<QuizBank> findAllByOrderByCreatedAtDesc();//moi nhat
    List<QuizBank> findAllByOrderByCreatedAtAsc();
}

