package com.quizify.service;

import com.quizify.model.Category;
import com.quizify.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(int id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void deleteCategory(int id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            categoryRepository.deleteById(id);
        }
    }
}
