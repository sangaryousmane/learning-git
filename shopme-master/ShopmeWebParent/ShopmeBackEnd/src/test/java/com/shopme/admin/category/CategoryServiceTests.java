/**
 * This Class was created to test the service class.
 * Using Mockito, we can inject a fake repo.
 */
package com.shopme.admin.category;


import com.shopme.admin.category.repository.CategoryRepository;
import com.shopme.admin.category.util.CategoryService;
import com.shopme.common.entity.Category;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
public class CategoryServiceTests {

    @MockBean
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;


    @Test
    public void testUniqueInNewModeAndReturnDuplicateName() {
        Integer id = null;
        String name = "Computers";
        String alias = "abc";

        Category category = new Category(id, name, alias);
        Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);

        String result = categoryService.checkUniqueCategory(id, name, alias);
        System.out.println(category);
        Assertions.assertThat(result).isEqualTo("DuplicateName");
    }

    @Test
    public void testUniqueInNewModeAndReturnDuplicateAlias() {
        Integer id = null;
        String name = "NameABC";
        String alias = "computers";

        Category category = new Category(id, name, alias);
        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);

        String result = categoryService.checkUniqueCategory(id, name, alias);
        System.out.println(category);
        Assertions.assertThat(result).isEqualTo("DuplicateAlias");
    }

    @Test
    public void testUniqueInNewModeAndReturnOk() {
        Integer id = null;
        String name = "NameABC";
        String alias = "computers";

        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);

        String result = categoryService.checkUniqueCategory(id, name, alias);
        Assertions.assertThat(result).isEqualTo("OK");
    }

    @Test
    public void testUniqueInEditModeAndReturnDuplicateAlias() {
        Integer id = 1;
        String name = "NameABC";
        String alias = "computers";

        Category category = new Category(2, name, alias);
        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);

        String result = categoryService.checkUniqueCategory(id, name, alias);
        System.out.println(category);
        Assertions.assertThat(result).isEqualTo("DuplicateAlias");
    }

    @Test
    public void testUniqueInEditModeAndReturnDuplicateName() {
        Integer id = 1;
        String name = "Computers";
        String alias = "abc";

        Category category = new Category(2, name, alias);
        Mockito.when(categoryRepository.findByName(name)).thenReturn(category);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(null);

        String result = categoryService.checkUniqueCategory(id, name, alias);
        System.out.println(category);
        Assertions.assertThat(result).isEqualTo("DuplicateName");
    }

    @Test
    public void testUniqueInEditModeAndReturnOk() {
        Integer id = 1;
        String name = "NameABC";
        String alias = "computers";

        Category category=new Category(id, name, alias);
        Mockito.when(categoryRepository.findByName(name)).thenReturn(null);
        Mockito.when(categoryRepository.findByAlias(alias)).thenReturn(category);

        String result = categoryService.checkUniqueCategory(id, name, alias);
        System.out.println(category);
        Assertions.assertThat(result).isEqualTo("OK");
    }
}








