package com.quizify.service;
import java.util.Optional;
import com.quizify.model.Category;
import com.quizify.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository CategoryRepository;

    public List<Category> getAllCategories() {
        return CategoryRepository.findAll();
    }
    public void deleteCategoryById(Integer id) {
        CategoryRepository.deleteCategoryById(id);
    }
    public Category createCategory(Category category) {
        return CategoryRepository.save(category);
    }

    public Category updateCategory(Integer id, String categoryName) {
        Optional<Category> optionalCategory = CategoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setCategoryName(categoryName);
            return CategoryRepository.save(category);
        } else {
            throw new RuntimeException("Category not found with id " + id);
        }
    }
    // Other service methods
}