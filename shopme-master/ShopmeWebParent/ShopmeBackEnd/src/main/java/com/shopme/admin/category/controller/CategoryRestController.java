package com.shopme.admin.category.controller;

import com.shopme.admin.category.util.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {



    private final CategoryService categoryService;

    @Autowired
    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories/check_unique")
    public String checkUnique(@Param("id") Integer id,
                              @Param("name") String name,
                              @Param("alias") String alias) {
        return categoryService.checkUniqueCategory(id, name, alias);
    }

}
