package com.david.inventory.repository;

import com.david.inventory.model.Category;
import com.david.inventory.model.Product;
import com.david.inventory.model.StockLog;
import com.david.inventory.model.StockLogType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class StockLogRepositoryTest {

    @Autowired
    private StockLogRepository stockLogRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveStockLog() {
        Category c = new Category();
        c.setName("Office");
        categoryRepository.save(c);

        Product p = new Product();
        p.setName("Chair");
        p.setSku("CH001");
        p.setQuantity(10);
        p.setCategory(c);
        productRepository.save(p);

        StockLog log = new StockLog();
        log.setProduct(p);
        log.setQuantityChange(5);
        log.setType(StockLogType.ADJUSTMENT);
        log.setNotes("Restock");

        StockLog saved = stockLogRepository.save(log);

        Assertions.assertNotNull(saved.getId());
    }
}
