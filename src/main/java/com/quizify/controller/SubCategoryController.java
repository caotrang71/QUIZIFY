package com.quizify.controller;

import com.quizify.model.*;
import com.quizify.repository.CategoryRepository;
import com.quizify.repository.SubcategoryRepository;
import com.quizify.service.*;
import jakarta.servlet.http.HttpSession;
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
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/subcategory/{id}")
    public String showSubCategory(@PathVariable int id, Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user.getRole().getId() == 1 || user.getRole().getId() ==2) {
            List<Subcategory> listSubCategory = subcategoryService.getSubcategoriesByCategoryId(id);
            if (listSubCategory != null) {
                model.addAttribute("listSubCategory", listSubCategory);
            }
            return "SubCategory-List";
        }else {
            return "error";
        }
    }

    @DeleteMapping("/subcategory/{id}")
    @ResponseBody
    public void deleteUser(@PathVariable int id,HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user.getRole().getId() == 1 || user.getRole().getId() ==2) {
            subcategoryService.deleteSubCateogry(id);
        }
    }
    @GetMapping("/show/update/subCategory/{idSubCate}")
    public String showUpdateSubcategory(@PathVariable int idSubCate,
                                        Model model,
                                        HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user.getRole().getId() == 1 || user.getRole().getId() ==2) {
            Subcategory subcategory = subcategoryRepository.findById(idSubCate).orElse(null);
            List<Category> category = categoryRepository.findAll();
            model.addAttribute("subCate", subcategory);
            model.addAttribute("Cate", category);
            return "updateSubcategory";
        }else {
            return "error";
        }
    }

    @PostMapping("/update/subCategory")
    public String updateSubCategory(@RequestParam("subCategoryId") int subCategoryId,
                                  @RequestParam("subCate") String subCategoryName,
                                  @RequestParam int cateId,
                                    RedirectAttributes redirectAttributes){

        Subcategory subcategory = subcategoryRepository.findById(subCategoryId).orElse(null);
        Category category = categoryRepository.findById(cateId).orElse(null);
        if (subcategory != null){
            subcategory.setSubcategoryName(subCategoryName);
            subcategory.setCategory(category);
            subcategoryRepository.save(subcategory);
            redirectAttributes.addFlashAttribute("message", "update subcategory successflly");
            return "redirect:/category";
        }else {
            return "error";
        }
    }

    @GetMapping("/show/create/subCategory")
    public String showPageCreateSubcategory(Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user.getRole().getId() == 1 || user.getRole().getId() ==2) {
            List<Category> category = categoryRepository.findAll();
            model.addAttribute("subCate", new Subcategory());
            model.addAttribute("cate", category);
            return "createSubcategory";
        }else {
            return "error";
        }
    }

    @PostMapping("/create/subCategory")
    public String createSubCategory(@ModelAttribute Subcategory subcategory,
                                    RedirectAttributes redirectAttributes){
        subcategoryRepository.save(subcategory);
        redirectAttributes.addFlashAttribute("message","create subcategory successfully");
        return "redirect:/category";
    }


}
