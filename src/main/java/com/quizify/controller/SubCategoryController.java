package com.quizify.controller;

import com.quizify.model.*;
import com.quizify.repository.SubcategoryRepository;
import com.quizify.service.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class SubCategoryController {
    @Autowired
    private SubcategoryService subcategoryService;
    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @GetMapping("/subcategory/{id}")
    public String showSubCategory(@RequestParam int id,Model model){
        List<Subcategory> listSubCategory = subcategoryService.getSubcategoriesByCategoryId(id);
        if (listSubCategory != null) {
            model.addAttribute("listSubCategory", listSubCategory);
        }
        return "SubCategory-List";
    }

    @DeleteMapping("/subcategory/{id}")
    @ResponseBody
    public void deleteUser(@PathVariable int id) {
        subcategoryService.deleteSubCateogry(id);
    }

    @GetMapping("/show/update/subCategory/{id}")
    public String showUpdateSubcategory(@PathVariable int id,
                                        Model model){
        Subcategory subcategory = subcategoryRepository.findById(id).orElse(null);
        model.addAttribute("subCate",subcategory);
        return "updateSubcategory";
    }

    @PostMapping("/update/subCategory")
    public void updateSubCategory(@RequestParam int subCategoryid,
                                    @RequestParam String subCategoryName){

        Subcategory subcategory = subcategoryRepository.findById(subCategoryid).orElse(null);
        if (subcategory != null){
            subcategory.setSubcategoryName(subCategoryName);
            subcategoryRepository.save(subcategory);
        }
    }

    @GetMapping("/show/create/subCategory")
    public String showPageCreateSubcategory(Model model){
        model.addAttribute("subcate", new Subcategory());
        return "createSubcategory";
    }

    @PostMapping("/create/subCategory")
    public String createSubCategory(@ModelAttribute Subcategory subcategory,
                                    RedirectAttributes redirectAttributes){
        subcategoryRepository.save(subcategory);
        redirectAttributes.addFlashAttribute("mess","create successfully");
        return "redirect:/subcategory";
    }


}
