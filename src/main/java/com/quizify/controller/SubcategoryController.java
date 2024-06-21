package com.quizify.controller;

import com.quizify.model.Subcategory;
import com.quizify.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SubcategoryController {

    @Autowired
    private SubcategoryService subcategoryService;

    @GetMapping("/manage-subcategory")
    public String getSubcategories(Model model, @RequestParam(value = "key", required = false) String key) {
        List<Subcategory> subcategories;
        if (key != null && !key.isEmpty()) {
            subcategories = subcategoryService.searchSubcategories(key);
        } else {
            subcategories = subcategoryService.getAllSubcategories();
        }
        model.addAttribute("subcategories", subcategories);
        return "manage-subcategory"; // This should match the name of your HTML file without the .html extension
    }

    @GetMapping("/create-subcategory")
    public String showPageCreateSubcategory(Model model) {
        model.addAttribute("subcategory", new Subcategory());
        return "create-subcategory"; // This should match the name of your create HTML file without the .html extension
    }

    @PostMapping("/created-subcategory")
    public String createSubcategory(@ModelAttribute Subcategory subcategory, Model model) {
        if (subcategoryService.checkSubcategory(subcategory.getSubcategoryName())) {
            subcategoryService.createSubcategory(subcategory);
            return "redirect:/manage-subcategory";
        } else {
            model.addAttribute("message", "Subcategory already exists!");
            return "redirect:/create-subcategory";
        }
    }

    @GetMapping("/update-subcategory/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Subcategory subcategory = subcategoryService.getSubcategoryById(id);
        model.addAttribute("subcategory", subcategory);
        return "update-subcategory"; // This should match the name of your update HTML file without the .html extension
    }

    @PostMapping("/update-subcategory")
    public String updateSubcategory(@ModelAttribute("subcategory") Subcategory subcategory) {
        subcategoryService.updateSubcategory(subcategory);
        return "redirect:/manage-subcategory";
    }
}