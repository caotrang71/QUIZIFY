package com.quizify.repository;
import java.util.Optional;
import com.quizify.model.Category;
import com.quizify.model.Subcategory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    public List<Subcategory> getAllCategories();
    @Modifying
    @Transactional
    @Query("DELETE FROM Category c WHERE c.categoryId = :id")
    void deleteCategoryById(Integer id);

}