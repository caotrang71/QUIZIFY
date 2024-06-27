package com.quizify.controller;

import com.quizify.model.*;
import com.quizify.repository.CategoryRepository;
import com.quizify.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/category")
    public String ShowCategory(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("listCategories", categories);
        return "category";
    }

    @DeleteMapping("/category/{id}")
    @ResponseBody
    public void deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
    }

    @GetMapping("/show//update/category/{id}")
    public String ShowUpdateCategory(Model model,
                                     @PathVariable int id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category != null) {
            model.addAttribute("category", category);
            return "updateCategory";
        }else{
            return "error";
        }
    }
    @PostMapping("/update/category")
    public String UpdateCategory( @RequestParam int categoryId,
                                  @RequestParam String categoryName,
                                  RedirectAttributes redirectAttributes) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category != null) {
            category.setCategoryName(categoryName);
            categoryRepository.save(category);
            redirectAttributes.addFlashAttribute("message", "Category Updated");
            return "redirect:/category";
        }else {
            return "error";
        }
    }

    @GetMapping("/show/create/category")
    public String showPageCreateCategory(Model model) {
        model.addAttribute("category", new Category());
        return "createCategory";
    }

    @PostMapping("/create/category")
    public String createCategory(@ModelAttribute Category category,
                                 RedirectAttributes redirectAttributes
    ) {
        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("message", "Create successfully");

        return "redirect:/category";
    }
}
