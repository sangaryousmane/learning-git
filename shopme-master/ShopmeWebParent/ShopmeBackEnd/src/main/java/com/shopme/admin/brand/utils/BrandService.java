package com.shopme.admin.brand.utils;

import com.shopme.admin.brand.repository.BrandRepository;
import com.shopme.common.entity.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class BrandService {

    public static final int BRAND_PER_PAGE = 4;
    private final BrandRepository brandRepository;

    @Autowired
    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Iterable<Brand> listAllBrands() {
        return brandRepository.findAll();
    }

    public Page<Brand> listByPage(
            int pageNum, String sortField,
            String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, BRAND_PER_PAGE, sort);

        if (keyword != null) {
            return brandRepository.findAll(keyword, pageable);
        }
        return brandRepository.findAll(pageable);
    }

    public Brand getBrandById(Integer id) throws BrandNotFoundException {
        try {
            return brandRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new BrandNotFoundException("Couldn't find any brand with the Id of " + id);
        }
    }

    public void deleteBrand(Integer id) throws BrandNotFoundException {
        Long countById = brandRepository.countById(id);
        if (countById != null && countById == 0) {
            throw new BrandNotFoundException("Couldn't find any Brand with the Id of" + id);
        }
        brandRepository.deleteById(id);
    }

    public Brand saveBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    // Check Unique Brand
    public String checkUniqueBrand(Integer id, String name) {
        boolean isCreatingNew = (id == null || id == 0);
        Brand findByName = brandRepository.findByName(name);

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
}
