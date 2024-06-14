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
//    void saveQuizBank(QuizBank quizBank);
 //   List<QuizBank> getQuizBanksListWithQuestions();

    QuizBank getQuizBankById(long id);
    void deleteQuizBanksById(long id);
//    Page<QuizBank> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
//    List<Question> getQuestions(QuizBank quizBank);

    List<QuizBank> getQuizBanksBySubcategory(Subcategory subcategory);

//    List<QuizBank> searchQuizBank(String keyword);
//    @Query("SELECT q FROM QuizBank q WHERE q.bankName LIKE %:keyword%")
//    Page<QuizBank> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
}

