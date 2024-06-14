package com.quizify.service;

import com.quizify.model.Category;
import com.quizify.model.Subcategory;
import com.quizify.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubcategoryService {

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    public List<Subcategory> getAllSubcategories() {
        return subcategoryRepository.findAll();
    }
    public Subcategory createSubcategory(Subcategory subcategory) {
        return subcategoryRepository.save(subcategory);
    }

    public Subcategory updateSubcategory(Integer id, String subcategoryName, Category category) {
        Optional<Subcategory> optionalSubcategory = subcategoryRepository.findById(id);
        if (optionalSubcategory.isPresent()) {
            Subcategory subcategory = optionalSubcategory.get();
            subcategory.setSubcategoryName(subcategoryName);
            subcategory.setCategory(category);
            return subcategoryRepository.save(subcategory);
        } else {
            throw new RuntimeException("Subcategory not found with id " + id);
        }
    }

    public void deleteSubcategoryById(Integer id) {
        subcategoryRepository.deleteSubcategoryById(id);
    }
}