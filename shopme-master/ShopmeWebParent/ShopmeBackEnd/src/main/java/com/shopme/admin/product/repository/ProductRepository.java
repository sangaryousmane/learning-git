package com.shopme.admin.product.repository;
import com.shopme.common.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.name=:name")
    Product findByName(@Param("name") String name);

    @Query("UPDATE Product p SET p.enabled=?2 WHERE p.id=?1")
    @Modifying
    void updateProduct(@Param("id") Integer id, @Param("enabled") boolean enabled);
    Long countById(Integer id);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1% " +
            "OR p.shortDescription LIKE %?1%" +
            "OR p.fullDescription LIKE %?1% " +
            "OR p.brand.name LIKE %?1% " +
            "OR p.category.name LIKE %?1%")
    Page<Product> findAll(String keyword, Pageable pageable);

}