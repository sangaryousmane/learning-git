package com.shopme.admin.category.controller;

import com.shopme.admin.category.exporter.CategoryCsvExporter;
import com.shopme.admin.category.util.CategoryNotFoundException;
import com.shopme.admin.category.util.CategoryPageInfor;
import com.shopme.admin.category.util.CategoryService;
import com.shopme.admin.FileUploadUtil;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @GetMapping("/categories")
    public String listByFirstPage(
            @Param("sortDir") String sortDir,
            Model model) {
        return listByPage(1, model, sortDir, null);
    }

    @GetMapping("/categories/new")
    public String createForm(Model model) {
        Iterable<Category> listCategories =
                categoryService.listAllCategoriesInForm();
        Category category = new Category();

        model.addAttribute("category", category);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("pageTitle", "Create New Category");
        return "categories/category_form";
    }

    // File And Image Upload
    private void fileAndPhotoUpload(
            Category category,
            @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
        // Upload File to the form if not empty.
        String fileName = StringUtils.cleanPath((Objects.requireNonNull
                (multipartFile.getOriginalFilename())));
        category.setImage(fileName);
        Category savedCategory = categoryService.save(category);
        String uploadDir = "../category-images/" + savedCategory.getId();
        FileUploadUtil.cleanDir(uploadDir);
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
    }

    @PostMapping("/categories/save")
    public String saveCategory(
            Category category,
            @RequestParam("fileImage") MultipartFile multipartFile,
            RedirectAttributes redirectAttributes,
            Model model) throws IOException {

        if (!multipartFile.isEmpty()) {
            fileAndPhotoUpload(category, multipartFile);
        } else {
            categoryService.save(category);
        }
        redirectAttributes.addFlashAttribute("message",
                "The Category with the ID: " + category.getId()
                        + " has been saved successfully");
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String editCategory(
            @PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            Category category = categoryService.getUserbyId(id);
            Iterable<Category> listCategories =
                    categoryService.listAllCategoriesInForm();
            model.addAttribute("category", category);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("pageTitle", "Edit Category With (ID: " + id + ")");

            return "categories/category_form";
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/categories";
        }
    }

    @GetMapping("/delete/category/{id}")
    public String deleteCategory(
            Model model,
            RedirectAttributes redirectAttributes,
            @PathVariable("id") Integer id) {

        try {
            categoryService.deleteCategory(id);
            String categoryDir = "../category-images/" + id;
            FileUploadUtil.removeDir(categoryDir);
            redirectAttributes.addFlashAttribute("message",
                    "The category with the ID of " + id + " has been deleted successfully.");
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/categories";
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String enabledUpdate(
            @PathVariable("id") Integer id,
            @PathVariable("status") boolean enabled,
            RedirectAttributes redirectAttributes) {

        categoryService.updateEnabled(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The Category with the Id of: " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/categories";
    }

    @GetMapping("/categories/page/{pageNum}")
    public String listByPage(
            @PathVariable("pageNum") int pageNum,
            Model model, @Param("sortDir") String sortDir,
            @Param("keyword") String keyword) {

        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }
        CategoryPageInfor pageInfo = new CategoryPageInfor();
        Iterable<Category> categoryList = categoryService.listByPage(pageInfo, pageNum, sortDir, keyword);
        int startCount = (pageNum - 1) * CategoryService.ROOT_CATEGORIES_PER_PAGE + 1;
        long endCount = startCount + CategoryService.ROOT_CATEGORIES_PER_PAGE - 1;
        if (endCount > pageInfo.getTotalElements()) {
            endCount = pageInfo.getTotalElements();
        }
        String reverseSortSir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("totalPages", pageInfo.getTotalPages());
        model.addAttribute("totalItems", pageInfo.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", "name");
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("reverseSortSir", reverseSortSir);
        return "categories/categories";
    }

    //Category export handler
    @GetMapping("/categories/export/csv")
    public void exportCsv(HttpServletResponse httpServletResponse) throws IOException {
        Iterable<Category> listCategories=categoryService.listAllCategoriesInForm();
        CategoryCsvExporter csvExporter=new CategoryCsvExporter();
        csvExporter.export(listCategories, httpServletResponse);
    }

}

