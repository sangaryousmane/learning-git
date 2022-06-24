package com.shopme.admin.brand.repository;

import com.shopme.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends
        PagingAndSortingRepository<Brand, Integer> {

    Long countById(Integer id);

    @Query("SELECT b FROM Brand b WHERE b.name=:name")
    Brand findByName(@Param("name") String name);

    @Query("SELECT b FROM Brand b WHERE b.name LIKE %?1%")
    Page<Brand> findAll(String keyword, Pageable pageable);

    // JPQL projection
    @Query("SELECT NEW Brand(b.id, b.name) FROM Brand b ORDER BY b.name ASC")
    Iterable<Brand> findAll();
}

/*
 Steps to implement paging and sorting in spring-boot
 1. declare a method in the repository with a string and
 the pageable interface as a parameter.

 2. Code the listByPage method in the service class with four parameters
   three strings(sortField, sortDir, keyword) and one integer(pageNum).
   Code the logic afterwards
    public Page<Brand> listByPage(
            int pageNum, String sortField,
            String sortDir, String keyword){
        Sort sort=Sort.by(sortField);
        sort=sortDir.equals("asc") ? sort.ascending():sort.descending();
        Pageable pageable= PageRequest.of(pageNum - 1, BRAND_PER_PAGE, sort);

        if (keyword !=null){
            return brandRepository.findAll(keyword, pageable);
        }
        return brandRepository.findAll(pageable);
    }

 3.
 */