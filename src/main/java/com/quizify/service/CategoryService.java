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
        Category cate = CategoryRepository.findById(id).orElse(null);
        if (cate != null) {
            CategoryRepository.deleteById(id);
        }
    }
    public Category getCategoryById(int id) {
        return CategoryRepository.findById(id).orElse(null);
    }
    public Category createCategory(Category category) {
        return CategoryRepository.save(category);
    }

    public boolean checkCategory(String name) {
        List<Category> listUser =CategoryRepository.findByCategoryName(name);
        if (listUser.isEmpty()) {
            return true;
        }else {
            return false;
        }
    }

    public void updateCategory(Category category) {
        CategoryRepository.save(category);
    }

    public List<Category> searchCategories(String key) {
        return CategoryRepository.findByCategoryNameContainingIgnoreCase(key);
    }
}