package com.david.inventory.repository;

import com.david.inventory.model.Category;
import com.david.inventory.model.Product;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveProduct() {
        Category c = new Category();
        c.setName("Hardware");
        categoryRepository.save(c);

        Product p = new Product();
        p.setName("Keyboard");
        p.setSku("KB001");
        p.setQuantity(5);
        p.setCategory(c);

        Product saved = productRepository.save(p);

        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals("Keyboard", saved.getName());
    }
}
