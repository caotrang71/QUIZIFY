package com.quizify.repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.quizify.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Integer> {
    public List<Subcategory> getAllSubcategories();
    @Modifying
    @Transactional
    @Query("DELETE FROM Subcategory s WHERE s.subcategoryId = :id")
    void deleteSubcategoryById(Integer id);
}