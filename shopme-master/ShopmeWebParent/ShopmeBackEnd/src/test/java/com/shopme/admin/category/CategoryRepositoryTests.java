package com.shopme.admin.category;

import com.shopme.admin.category.repository.CategoryRepository;
import com.shopme.common.entity.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    public void testCreateRootCategory() {
        Category category = new Category("Electronics");
        Category savedCategory = categoryRepository.save(category);
        Assertions.assertThat(savedCategory.getId()).isGreaterThan(0);

    }

    @Test
    public void testCreateSubCategory1() {
        Category parent = new Category(1);
        Category laptops = new Category("Laptops", parent);
        Category computer_components = new Category("Computer Components", parent);

        categoryRepository.saveAll(List.of(laptops, computer_components));
    }

    @Test
    public void testCreateSubCategory2() {
        Category parent = new Category(2);
        Category cameras = new Category("Cameras", parent);
        Category smart_phones = new Category("Smart Phones", parent);

        categoryRepository.saveAll(List.of(cameras, smart_phones));
    }

    @Test
    public void testCreateSubChildCategory() {
        Category parent = new Category(7);
        Category memory = new Category("Iphone", parent);
        Category savedCategory = categoryRepository.save(memory);

        Assertions.assertThat(savedCategory).isNotNull();
    }

    @Test
    public void testGetCategory() {
        Category parent = categoryRepository.findById(1).get();
        System.out.println(parent.getName());

        Set<Category> children = parent.getChildren();

        for (Category subCategory : children) {
            System.out.println(subCategory.getName());
        }
    }

    @Test
    public void testPrintHierarchicalCategories() {
        Iterable<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            if (category.getParent() == null) {
                System.out.println(category.getName());


                Set<Category> children = category.getChildren();
                for (Category subCategory : children) {
                    System.out.println("--" + subCategory.getName());
                    printChildren(subCategory, 1);
                }
            }
        }
    }

    private void printChildren(Category parent, int subLevel) {
        int newSubLevel = subLevel + 1;
        Set<Category> children = parent.getChildren();

        for (Category subCategory : children) {

            for (int i = 0; i < newSubLevel; i++) {
                System.out.println("---");
            }
            System.out.println(subCategory.getName());
            printChildren(subCategory, newSubLevel);
        }
    }


    @Test
    public void testListRootCategories() {
      List<Category> rootCategories=categoryRepository.findRootCategories(Sort.by("name").ascending());

      rootCategories.forEach(category -> {
          System.out.println(category.getName());
      });
    }

    @Test
    public void createTestFindByName(){
        String name="Computers";
        Category findByName=categoryRepository.findByName(name);

        System.out.println(findByName);
        Assertions.assertThat(findByName).isNotNull();
        Assertions.assertThat(findByName.getName()).isEqualTo(name);
    }

    @Test
    public void findByAlias(){
        String alias="Cameras";
        Category findByAlias=categoryRepository.findByAlias(alias);

        System.out.println(findByAlias);
        Assertions.assertThat(findByAlias).isNotNull();
        Assertions.assertThat(findByAlias.getAlias()).isEqualTo(alias);
    }
}
