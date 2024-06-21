package com.quizify.controller;
import com.quizify.model.Category;
import com.quizify.repository.CategoryRepository;
import com.quizify.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService CategoryService;
    @Autowired
    private CategoryRepository CategoryRepository;

    @GetMapping("/Home")
    public String ShowHomepage(Model model) {
        return "homePage";
    }

    @GetMapping("/")
    public String viewLandingPage(Model model) {
        return "landing-page";
    }

    @GetMapping("/manage_category")
    public String getListCategory(Model model, @RequestParam(value = "key", required = false) String key) {
        List<Category> categories;
        if (key != null && !key.isEmpty()) {
            categories = CategoryService.searchCategories(key);
        } else {
            categories = CategoryService.getAllCategories();
        }
        System.out.println(categories);
        model.addAttribute("categories", categories);
        return "manage-category";
    }

    @DeleteMapping("/manage-category/{id}")
    @ResponseBody
    public void deleteCategory(@PathVariable int id) {
        CategoryService.deleteCategoryById(id);
    }

    @GetMapping("/view_category/{id}")
    public String viewCategory(@PathVariable int id, Model model) {
        Category category = CategoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "category-detail";
    }

    @GetMapping("/create-category")
    public String showPageCreateCategory(Model model) {
        model.addAttribute("category", new Category());
        return "create-category";
    }

    @PostMapping("/created")
    public String createCategory(@ModelAttribute Category category, Model model) {
        if (CategoryService.checkCategory(category.getCategoryName())){
            CategoryRepository.save(category);
            return "redirect:/manage_category";
        }else {
            model.addAttribute("message","Category is existed!");
            return "redirect:/create_category";
        }
    }
    @GetMapping("/search")
    public String searchCategory(@RequestParam("key") String key, Model model) {
        List<Category> categories = CategoryRepository.findByCategoryName(key);
        List<Category> categoriesF = CategoryRepository.findByCategoryNameContainingIgnoreCase(key);
        if (!categories.isEmpty()) {
            model.addAttribute("searchCate", categories);
        }
        else if (!categoriesF.isEmpty()) {
            model.addAttribute("searchCate", categoriesF);
        }
        return "manage-category";
    }
    @GetMapping("/update-category/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Category category = CategoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "update-category"; // This should match the name of your update HTML file without the .html extension
    }

    @PostMapping("/update-category")
    public String updateCategory(@ModelAttribute("category") Category category) {
        CategoryService.updateCategory(category);
        return "redirect:/manage-category";
    }
}