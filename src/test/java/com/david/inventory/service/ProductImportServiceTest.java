package com.david.inventory.service;

import com.david.inventory.dto.ProductResponse;
import com.david.inventory.exception.InvalidExcelException;
import com.david.inventory.model.Category;
import com.david.inventory.model.Product;
import com.david.inventory.model.Supplier;
import com.david.inventory.repository.CategoryRepository;
import com.david.inventory.repository.ProductRepository;
import com.david.inventory.repository.SupplierRepository;
import com.david.inventory.util.ExcelFactoryTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@SpringBootTest
@Transactional
public class ProductImportServiceTest {

    @Autowired
    private ProductImportService service;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private SupplierRepository supplierRepo;

    @Autowired
    private ProductRepository productRepo;

    @Test
    void testImportExcel() throws Exception {
        Category category = categoryRepo.save(new Category(null, "Hardware", ""));
        Supplier supplier = supplierRepo.save(new Supplier(null, "Tech", "tech@x.com", "", ""));

        MultipartFile file = ExcelFactoryTest.createValidExcel(category.getId(), supplier.getId());

        List<ProductResponse> responses = service.importExcel(file);

        Assertions.assertEquals(2, responses.size());
        Assertions.assertEquals(2, productRepo.count());
    }

    @Test
    void testMissingRequiredField() throws Exception {
        MultipartFile file = ExcelFactoryTest.createExcelMissingField();

        Assertions.assertThrows(InvalidExcelException.class, () -> {
            service.importExcel(file);
        });

        Assertions.assertEquals(0, productRepo.count()); // rollback
    }

    @Test
    void testInvalidFormat() throws Exception {
        MultipartFile file = ExcelFactoryTest.createExcelInvalidFormat();

        Assertions.assertThrows(InvalidExcelException.class, () -> {
            service.importExcel(file);
        });
    }

    @Test
    void testDuplicateSku() throws Exception {
        Category c = categoryRepo.save(new Category(null, "Hardware", ""));
        Supplier s = supplierRepo.save(new Supplier(null, "Tech", "tech@x.com", "", ""));

        productRepo.save(Product.builder()
                .name("Test")
                .price(10)
                .sku("SKU1")
                .category(c)
                .supplier(s)
                .quantity(1)
                .build());

        MultipartFile file = ExcelFactoryTest.createExcelDuplicateSku();

        Assertions.assertThrows(InvalidExcelException.class, () -> {
            service.importExcel(file);
        });
    }

}
