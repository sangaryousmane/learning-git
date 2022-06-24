package com.shopme.admin.brand.controller;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.utils.BrandNotFoundException;
import com.shopme.admin.brand.utils.BrandService;
import com.shopme.admin.category.util.CategoryPageInfor;
import com.shopme.admin.category.util.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import java.io.IOException;
import java.util.Objects;

@Controller
public class BrandController {

    private final BrandService brandService;
    private final CategoryService categoryService;

    @Autowired
    public BrandController(BrandService brandService,
                           CategoryService categoryService) {
        this.brandService = brandService;
        this.categoryService = categoryService;
    }

    @GetMapping("/brands")
    public String getAllBrands(Model model) {
        Iterable<Brand> listAllBrands = brandService.listAllBrands();
        Iterable<Category> listCategories = categoryService.listAllCategoriesInForm();
        model.addAttribute("listAllBrands", listAllBrands);
        model.addAttribute("listCategories", listCategories);
        return "brands/brands";
    }

    @GetMapping("/brands/createNew")
    public String createNewBrand(Model model) {
        Iterable<Category> listCategories = categoryService.listAllCategoriesInForm();
        Brand brand = new Brand();
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("brand", brand);
        model.addAttribute("pageTitle", "Create New Brand");
        return "brands/brands_form";
    }

    @GetMapping("/brands/update/{id}")
    public String editBrands(
            @PathVariable("id") Integer id,
            Model model, RedirectAttributes redirectAttributes) {
        try {
            Brand brand = brandService.getBrandById(id);
            Iterable<Category> listCategories=categoryService.listAllCategoriesInForm();
            model.addAttribute("brand", brand);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("pageTitle", "Edit Brand (ID " + id + ")");

            return "brands/brands_form";
        } catch (BrandNotFoundException b) {
            redirectAttributes.addFlashAttribute("message", b.getMessage());
            return "redirect:/brands";
        }
    }

    @GetMapping("/brands/delete/{id}")
    public String deleteBrandById(
            @PathVariable("id") Integer id,
            RedirectAttributes redirectAttributes) {

        try {
            brandService.deleteBrand(id);
            String brandDir="../brand-logos/"+id;
            FileUploadUtil.removeDir(brandDir);
            redirectAttributes.addFlashAttribute("message",
                    "The brand with the ID of " + id + " has been deleted successfully.");
        } catch (BrandNotFoundException b) {
            redirectAttributes.addFlashAttribute("message", b.getMessage());
        }
        return "redirect:/brands";
    }

    private void fileAndPhotoUpload(
            Brand brand,
            @RequestParam("fileImage") MultipartFile multipartFile) throws IOException {
        // Upload File to the form if not empty.
        String fileName = StringUtils.cleanPath((Objects.requireNonNull
                (multipartFile.getOriginalFilename())));
        brand.setLogo(fileName);
        Brand savedBrand = brandService.saveBrand(brand);
        String uploadDir = "../brand-logos/" + savedBrand.getId();
        FileUploadUtil.cleanDir(uploadDir);
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
    }

    @PostMapping("/brands/save")
    public String saveBrand(Brand brand,
            @RequestParam("fileImage") MultipartFile multipartFile,
            RedirectAttributes redirectAttributes) throws IOException {
        if (!(multipartFile.isEmpty())) {
            fileAndPhotoUpload(brand, multipartFile);
        }
        else {
                brandService.saveBrand(brand);
            }
        redirectAttributes.addFlashAttribute("message",
                "The Brand has been saved successfully.");
            return "redirect:/brands";
        }


    // Paging and Sorting for Brands
    @GetMapping("/brands/page/{pageNum}")
    public String listByPage(
            @PathVariable("pageNum") int pageNum,
            Model model,
            @Param("sortDir") String sortDir,
            @Param("keyword") String keyword,
            @Param("sortField") String sortField) {

        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "asc";
        }
        Page<Brand> pageList = brandService.listByPage(pageNum, sortField, sortDir, keyword);
        int startCount = (pageNum - 1) * BrandService.BRAND_PER_PAGE+ 1;
        long endCount = startCount + BrandService.BRAND_PER_PAGE - 1;
        if (endCount > pageList.getTotalElements()) {
            endCount = pageList.getTotalElements();
        }
        String reverseSortSir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("totalPages", pageList.getTotalPages());
        model.addAttribute("totalItems", pageList.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", "name");
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("categoryList", pageList);
        model.addAttribute("reverseSortSir", reverseSortSir);
        return "categories/categories";
    }
}
