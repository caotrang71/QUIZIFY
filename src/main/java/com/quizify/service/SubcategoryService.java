package com.quizify.service;

import com.quizify.model.Category;
import com.quizify.model.QuizBank;
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

    public Subcategory getSubcategoryById(int id){
        Optional<Subcategory> optional = subcategoryRepository.findById(id);
        Subcategory subcategory = null;
        if (optional.isPresent()) {
            subcategory = optional.get();
        } else {
            throw new RuntimeException(" Quiz Bank not found for id :: " + id);
        }
        return subcategory;
   //     return this.subcategoryRepository.getReferenceById(id);
    }

    public List<Subcategory> getSubcategoriesByCategory(Category category) {
        return subcategoryRepository.getSubcategoriesByCategory(category);
    }



}