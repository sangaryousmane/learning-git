package com.shopme.admin.brands;

import com.shopme.admin.brand.repository.BrandRepository;
import com.shopme.admin.brand.utils.BrandService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BrandServiceTests {


    @MockBean private BrandRepository brandRepository;
    @InjectMocks private BrandService brandService;
}
