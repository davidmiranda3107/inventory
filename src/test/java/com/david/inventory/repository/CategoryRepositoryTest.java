package com.david.inventory.repository;

import com.david.inventory.model.Category;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveCategory() {
        Category c = new Category();
        c.setName("Electronics");

        Category saved = categoryRepository.save(c);

        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals("Electronics", saved.getName());
    }
}
