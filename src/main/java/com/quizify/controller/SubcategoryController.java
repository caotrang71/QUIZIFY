package com.quizify.controller;

import com.quizify.model.Subcategory;
import com.quizify.service.SubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subcategory")
public class SubcategoryController {

    @Autowired
    private SubcategoryService subcategoryService;

    @PostMapping
    public Subcategory createSubcategory(@RequestBody Subcategory subcategory) {
        return subcategoryService.createSubcategory(subcategory);
    }

    @PutMapping("/{id}")
    public Subcategory updateSubcategory(@PathVariable Integer id, @RequestBody Subcategory subcategoryDetails) {
        return subcategoryService.updateSubcategory(id, subcategoryDetails.getSubcategoryName(), subcategoryDetails.getCategory());
    }

    @DeleteMapping("/{id}")
    public void deleteSubcategory(@PathVariable Integer id) {
        subcategoryService.deleteSubcategoryById(id);
    }

    @GetMapping
    public List<Subcategory> getAllSubcategories() {
        return subcategoryService.getAllSubcategories();
    }
}