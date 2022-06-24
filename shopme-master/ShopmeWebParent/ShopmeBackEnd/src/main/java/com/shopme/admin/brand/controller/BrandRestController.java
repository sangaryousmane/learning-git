package com.shopme.admin.brand.controller;

import com.shopme.admin.brand.utils.BrandNotFoundException;
import com.shopme.admin.brand.utils.BrandNotFoundRestException;
import com.shopme.admin.brand.utils.BrandService;
import com.shopme.admin.brand.utils.CategoryDTO;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class BrandRestController {

    private final BrandService brandService;

    @Autowired
    public BrandRestController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping("/brands/check_unique")
    public String checkUnique(
            @Param("id") Integer id,
            @Param("name") String name) {
       return brandService.checkUniqueBrand(id, name);
    }

    @GetMapping("/brands/{id}/categories")
    public Iterable<CategoryDTO> listCategoriesByBrand(
            @PathVariable(name = "id") Integer id) throws BrandNotFoundRestException {

        try {
            List<CategoryDTO> listCategories = new ArrayList<>();
            Brand brand = brandService.getBrandById(id);
            Set<Category> categories = brand.getCategories();

            categories.forEach(category -> {
                CategoryDTO categoryDTO = new CategoryDTO(category.getId(), category.getName());
                listCategories.add(categoryDTO);
            });
            return listCategories;
        }
        catch (BrandNotFoundException b){
            throw new BrandNotFoundRestException();
        }

    }
}
