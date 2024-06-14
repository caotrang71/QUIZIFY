package com.quizify.controller;
import com.quizify.model.Category;
import com.quizify.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService CategoryService;

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return CategoryService.createCategory(category);
    }

    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Integer id, @RequestBody Category categoryDetails) {
        return CategoryService.updateCategory(id, categoryDetails.getCategoryName());
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Integer id) {
        CategoryService.deleteCategoryById(id);
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return CategoryService.getAllCategories();
    }
}
