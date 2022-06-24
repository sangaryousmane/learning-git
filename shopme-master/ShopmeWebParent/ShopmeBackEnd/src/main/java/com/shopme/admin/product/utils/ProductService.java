package com.shopme.admin.product.utils;

import com.shopme.admin.product.repository.ProductRepository;
import com.shopme.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@Transactional
public class ProductService {

    public static final int PRODUCT_PER_PAGE = 4;
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<Product> listAllProducts() {
        return (List<Product>) productRepository.findAll(Sort.by("name").ascending());
    }

    public Page<Product> listByPage(
            int pageNum, String sortField,
            String sortDir, String keyword) {

        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCT_PER_PAGE, sort);

        if (keyword != null) {
            return productRepository.findAll(keyword, pageable);
        }
        return productRepository.findAll(pageable);
    }

    public Product saveProducts(Product product) {
        if (product.getId() == null) {
            product.setCreatedTime(new Date());
        }
        if (product.getAlias() == null || product.getAlias().isEmpty()) {
            String defaultAlias = product.getName().replaceAll(" ", "-");
            product.setAlias(defaultAlias);
        } else {
            product.setAlias(product.getAlias().replaceAll(" ", "-"));
        }
        product.setUpdatedTime(new Date());
        return productRepository.save(product);
    }

    // Checking products uniqueness
    public String checkUniqueBrand(Integer id, String name) {
        boolean isCreatingNew = (id == null || id == 0);
        Product findByName = productRepository.findByName(name);

        if (isCreatingNew) {
            if (findByName != null)
                return "Duplicate";
        } else {
            if (findByName != null && !Objects.equals(findByName.getId(), id)) {
                return "Duplicate";
            }
        }
        return "OK";
    }

    //Enabled product
    public void updateProductEnabledStatus(Integer id, boolean enabled) {
        productRepository.updateProduct(id, enabled);
    }

    public void deleteProductById(Integer id) {
        Long countById = productRepository.countById(id);
        if (countById == null || countById == 0) {
            throw new ProductNotFoundException("Couldn't delete product with ID of " + id);
        }
        productRepository.deleteById(id);
    }

    public Product get(Integer id) throws ProductNotFoundException {
        try {
            return productRepository.findById(id).get();
        } catch (NoSuchElementException s) {
            throw new ProductNotFoundException("Couldn't find any product with ID of " + id);
        }
    }
}
