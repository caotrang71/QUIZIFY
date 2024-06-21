package com.quizify.service;

import com.quizify.model.Subcategory;
import com.quizify.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubcategoryService {

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    public List<Subcategory> getAllSubcategories() {
        return subcategoryRepository.findAll();
    }

    public Subcategory getSubcategoryById(Integer id) {
        return subcategoryRepository.findById(id).orElse(null);
    }

    public void updateSubcategory(Subcategory subcategory) {
        subcategoryRepository.save(subcategory);
    }

    public List<Subcategory> searchSubcategories(String key) {
        return subcategoryRepository.findBySubcategoryNameContaining(key);
    }

    public Subcategory createSubcategory(Subcategory subcategory) {
        return subcategoryRepository.save(subcategory);
    }

    public boolean checkSubcategory(String name) {
        List<Subcategory> listSubcategory = subcategoryRepository.findBySubcategoryNameContaining(name);
        return listSubcategory.isEmpty();
    }
}