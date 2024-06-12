package com.quizify.repository;

import com.quizify.model.QuizBank;
import com.quizify.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Integer> {
}
