package com.shopme.admin.product;

import com.shopme.admin.product.repository.ProductRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Commit;

import java.util.Date;
import java.util.Optional;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@Commit
public class ProductRepositoryTests {

    private final ProductRepository productRepository;
    private final TestEntityManager testEntityManager;

    @Autowired
    public ProductRepositoryTests(TestEntityManager testEntityManager,
                                  ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.testEntityManager = testEntityManager;

    }


    @Test
    void createProducts() {
        Brand brand = testEntityManager.find(Brand.class, 37);
        Category category = testEntityManager.find(Category.class, 5);
        Product newProduct = new Product();
        newProduct.setName("Acer apire desktop");
        newProduct.setAlias("acer aspire desktop 3000");
        newProduct.setShortDescription("A good Desktop from Acer");
        newProduct.setFullDescription("This is a very good Desktop from Acer, you won't regret giving it a try");
        newProduct.setBrand(brand);
        newProduct.setCategory(category);
        newProduct.setCreatedTime(new Date());
        newProduct.setEnabled(true);
        newProduct.setInStock(true);
        newProduct.setPrice(456);
        newProduct.setCost(400);
        newProduct.setUpdatedTime(new Date());
        Product saveProductObject = productRepository.save(newProduct);


        Assertions.assertThat(saveProductObject).isNotNull();
        Assertions.assertThat(saveProductObject.getId()).isGreaterThan(0);


    }

    @Test
    void testListAllProductsFromTheRepo() {
        Iterable<Product> allProducts = productRepository.findAll();
        allProducts.forEach(System.out::println);
        Assertions.assertThat(allProducts).isNotEmpty();
    }

    @Test
    void testFindProductById() {
        Integer id = 2;
        Optional<Product> findById = productRepository.findById(id);
        System.out.println(findById);
        Assertions.assertThat(findById).isNotNull();
    }

    @Test
    void testCreateUpdateProduct() {
        Integer productId = 1;
        Product findById = productRepository.findById(productId).get();
        findById.setPrice(499);

        Product saveProduct = productRepository.save(findById);
        Product productByClass = testEntityManager.find(Product.class, productId);

        Assertions.assertThat(productByClass.getPrice()).isEqualTo(499);
        Assertions.assertThat(saveProduct.getId()).isEqualTo(productId);

    }

    @Test
    void testDeleteProduct() {
        Integer id = 3;
        productRepository.deleteById(id);
        Optional<Product> result = productRepository.findById(id);
        System.out.println(result);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void testSaveProductsWithImages(){
        Integer productId= 2;
        Product findById = productRepository.findById(productId).get();
        findById.setMainImage("main Image.jpg");
        findById.addExtraImage("extra image_1.png");
        findById.addExtraImage("extra_image_2.png");
        findById.addExtraImage("extra-image_3.png");
        Product saveProductWithImage = productRepository.save(findById);
        Assertions.assertThat(saveProductWithImage.getImages().size()).isEqualTo(3);
    }

    @Test
    void testProductDetails(){
        Integer Id=4;
        Product getProductId=productRepository.findById(Id).get();
        getProductId.addProductDetails("Device Memory", "126GB");
        getProductId.addProductDetails("CPU Model", "MediaTek");
        getProductId.addProductDetails("OS", "Android 10");

        Product savedProduct=productRepository.save(getProductId);
        Assertions.assertThat(savedProduct.getProductDetails()).isNotEmpty();
    }
}
