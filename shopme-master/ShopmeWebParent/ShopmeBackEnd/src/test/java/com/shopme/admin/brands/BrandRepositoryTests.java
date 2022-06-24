package com.shopme.admin.brands;

import com.shopme.admin.brand.repository.BrandRepository;
import com.shopme.admin.brand.utils.BrandNotFoundException;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Commit
public class BrandRepositoryTests {

    private final BrandRepository brandRepository;


    @Autowired
    public BrandRepositoryTests(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }


    @Test
    void createNewBrands1() {
        Category category = new Category(6);
        Brand brandAcer = new Brand("Acer");
        brandAcer.getCategories().add(category);
        Brand saveBrand = brandRepository.save(brandAcer);

        Assertions.assertThat(saveBrand).isNotNull();
        Assertions.assertThat(saveBrand.getId()).isGreaterThan(0);
    }


    @Test
    void createNewBrands2(){
        Category cellphone=new Category(4);
        Category tablets=new Category(7);
        Category memory=new Category(29);
        Category internalHardDrives=new Category(24);
        Brand brandApple=new Brand("Apple");
        Brand brandSamsung=new Brand("SamSung");

        brandApple.getCategories().addAll(List.of(cellphone, tablets));
        brandSamsung.getCategories().addAll(List.of(memory, internalHardDrives));
        Brand savedBrandApple=brandRepository.save(brandApple);
        Brand savedBrandSamsung=brandRepository.save(brandSamsung);

        Assertions.assertThat(savedBrandApple).isNotNull();
        Assertions.assertThat(savedBrandSamsung.getId()).isGreaterThan(1);
    }

    @Test
    void findAllBrands(){
        Iterable<Brand> brandList=brandRepository.findAll();
        brandList.forEach(System.out::println);
        Assertions.assertThat(brandList).isNotEmpty();
    }
    @Test
    void findBrandsById(){
        Brand findById=brandRepository.findById(1).get();
        System.out.println(findById);
        Assertions.assertThat(findById.getName()).isEqualTo("Acer");

    }

    @Test
    void updateBrands(){
        String newName="SamSung Electronics";
        Brand findBrandById=brandRepository.findById(3).get();
        findBrandById.setName(newName);

        Brand savedNewBrand=brandRepository.save(findBrandById);
        Assertions.assertThat(savedNewBrand.getName()).isEqualTo(newName);
    }

    @Test
    void testDeleteBrands(){
        Integer brandId= 2;
        brandRepository.deleteById(brandId);
        Optional<Brand> result=brandRepository.findById(brandId);
        System.out.println(result);
        Assertions.assertThat(result).isEmpty();

    }


}
