package com.shopme.admin.product.controller;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.utils.BrandService;
import com.shopme.admin.category.util.CategoryService;
import com.shopme.admin.product.utils.ProductNotFoundException;
import com.shopme.admin.product.utils.ProductService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.common.entity.ProductImage;
import lombok.extern.slf4j.Slf4j;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
@Slf4j
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final BrandService brandService;

    @Autowired
    public ProductController(
            CategoryService categoryService,
            ProductService productService,
            BrandService brandService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.brandService = brandService;
    }


    // Display all products
    @GetMapping("/products")
    public String listFirstPage(Model model) {
        List<Product> productList=productService.listAllProducts();
        model.addAttribute("productList", productList);
        return "products/products";
    }

    // Create new product
    @GetMapping("/products/createNew")
    public String newProduct(Model model) {
        Iterable<Brand> brandList = brandService.listAllBrands();
        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("product", product);
        model.addAttribute("brandList", brandList);
        model.addAttribute("pageTitle", "Create New Product");
        return "products/product_form";
    }

    // Adding main Image.
    private void setMainImageName(
            MultipartFile mainImageMultiPartFile,
            Product product) {
        if (!mainImageMultiPartFile.isEmpty()) {
            String fileName = StringUtils
                    .cleanPath(Objects.requireNonNull(mainImageMultiPartFile.getOriginalFilename()));
            product.setMainImage(fileName);
        }
    }

    // Adding existingExtraImages to DB
    private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames,
                                            Product product) {
        if (imageIDs == null || imageIDs.length == 0) {
            return;
        }

        Set<ProductImage> images = new HashSet<>();
        for (int count = 0; count < imageIDs.length; count++) {
            Integer id = Integer.parseInt(imageIDs[count]);
            String name = imageNames[count];
            images.add(new ProductImage(id, name, product));
        }
        product.setImages(images);
    }


    // Adding extra Images.
    private void setNewExtraImageNames(
            MultipartFile[] extraImageMultiPartFile,
            Product product) {
        if (extraImageMultiPartFile.length > 0) {
            for (MultipartFile multipartFile : extraImageMultiPartFile) {
                if (!multipartFile.isEmpty()) {
                    String fileName = StringUtils
                            .cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));

                    if (!product.containsImageName(fileName)) {
                        product.addExtraImage(fileName);
                    }
                }
            }
        }
    }

    // Adding Product Details.
    private void setProductDetails(
            String[] detailIDs,
            String[] detailNames,
            String[] detailValues,
            Product product) {
        if (detailNames == null || detailNames.length == 0) return;

        for (int count = 0; count < detailNames.length; count++) {
            String name = detailNames[count];
            String value = detailValues[count];
            int id = Integer.parseInt(detailIDs[count]);

            if (id != 0) {
                product.addProductDetails(id, name, value);
            } else if (!name.isEmpty() && !value.isEmpty()) {
                product.addProductDetails(name, value);
            }
        }
    }

    // Saving uploaded image files.
    private void saveUploadedFiles(
            MultipartFile mainImageMultiPartFile,
            MultipartFile[] extraImageMultiPartFile,
            Product savedProduct) throws IOException {

        if (!mainImageMultiPartFile.isEmpty()) {
            String fileName = StringUtils
                    .cleanPath(Objects.requireNonNull(mainImageMultiPartFile.getOriginalFilename()));
            String uploadDir = "../product-images/" + savedProduct.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultiPartFile);
        }

        if (extraImageMultiPartFile.length > 0) {
            String uploadDir = "../product-images/" + savedProduct.getId() + "/extra";
            for (MultipartFile multipartFile : extraImageMultiPartFile) {
                if (multipartFile.isEmpty()) continue;
                String fileName = StringUtils
                        .cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

            }
        }
    }

    // Save Product
    @PostMapping("/products/save")
    public String saveProducts(
            Product product,
            RedirectAttributes redirectAttributes,
            @RequestParam("fileImage") MultipartFile mainImageMultiPartFile,
            @RequestParam("extraImage") MultipartFile[] extraImageMultiPartFile,
            @RequestParam(name = "detailIDs", required = false) String[] detailIDs,
            @RequestParam(name = "detailNames", required = false) String[] detailNames,
            @RequestParam(name = "detailValues", required = false) String[] detailValues,
            @RequestParam(name = "imageIDs", required = false) String[] imageIDs,
            @RequestParam(name = "imageNames", required = false) String[] imageNames) throws IOException {

        setMainImageName(mainImageMultiPartFile, product);
        setExistingExtraImageNames(imageIDs, imageNames, product);
        setNewExtraImageNames(extraImageMultiPartFile, product);
        setProductDetails(detailIDs, detailNames, detailValues, product);

        Product savedProduct = productService.saveProducts(product);
        saveUploadedFiles(mainImageMultiPartFile, extraImageMultiPartFile, savedProduct);
        deleteExtraImagesWereRemovedOnForm(product);
        redirectAttributes.addFlashAttribute("message",
                "The Product has been saved successfully.");
        return "redirect:/products";
    }

    private void deleteExtraImagesWereRemovedOnForm(Product product) {
        String extraImageDir = "../product-images/" + product.getId() + "/extra";
        Path dirPath = Paths.get(extraImageDir);

        try {
            Files.list(dirPath).forEach(file -> {
                String fileName = file.toFile().getName();
                if (!product.containsImageName(fileName)) {
                    try {
                        Files.delete(file);
                        log.info("Deleted extra image: " + fileName);
                    } catch (IOException e) {
                        log.error("Couldn't delete extra image " + fileName);
                    }
                }
            });
        } catch (IOException e) {
            log.error("Couldn't delete extra image " + dirPath);
        }

    }

    // Update Product
    @GetMapping("/products/{id}/enabled/{status}")
    public String productUpdate(
            @PathVariable("id") Integer id,
            @PathVariable("status") boolean enabled,
            RedirectAttributes redirectAttributes) {

        productService.updateProductEnabledStatus(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        String message = "The Product with the ID of " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/products";
    }

    // Delete Product
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(
            @PathVariable("id") Integer id,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProductById(id);
            String productExtraImagesDir = "../product-images/" + id + "/extras";
            String productImagesDir = "../product-images/" + id;
            FileUploadUtil.removeDir(productExtraImagesDir);
            FileUploadUtil.removeDir(productImagesDir);

            redirectAttributes.addFlashAttribute("message",
                    "The Product with the ID of " + id + " has been deleted successfully.");
        } catch (ProductNotFoundException p) {
            redirectAttributes.addFlashAttribute("message", p.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(
            @PathVariable("id") Integer id,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.get(id);
            Iterable<Brand> brandList = brandService.listAllBrands();
            Integer numberOfExistingExtraImages = product.getImages().size();

            model.addAttribute("product", product);
            model.addAttribute("brandList", brandList);
            model.addAttribute("pageTitle", "Edit Product (ID: " + id + ")");
            model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);
            return "products/product_form";
        } catch (ProductNotFoundException p) {
            redirectAttributes.addFlashAttribute("message", p.getMessage());
            return "redirect:/products";
        }
    }

    // Handler to code product details dialog
    @GetMapping("/products/detail/{id}")
    public String viewProductDetails(
            @PathVariable("id") Integer id,
            Model model,
            RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.get(id);

            model.addAttribute("product", product);
            return "products/product_detail_modal";
        } catch (ProductNotFoundException p) {
            redirectAttributes.addFlashAttribute("message", p.getMessage());
            return "redirect:/products";
        }
    }

    @GetMapping("/products/page/{pageNum}")
    public String listByPage(
            @PathVariable(name = "pageNum") int pageNum,
            Model model,
            @Param("sortField") String sortField,
            @Param("sortDir") String sortDir,
            @Param("keyword") String keyword) {

        Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword);
        List<Product> listProducts = page.getContent();
        Iterable<Category> listCategories=categoryService.listAllCategoriesInForm();
        long startCount = (long) (pageNum - 1) * ProductService.PRODUCT_PER_PAGE + 1;
        long endCount = startCount + ProductService.PRODUCT_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("listCategories", listCategories);
        return "products/products";
    }
}
