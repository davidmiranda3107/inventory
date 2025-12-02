package com.david.inventory.repository;

import com.david.inventory.model.Supplier;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class SupplierRepositoryTest {

    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    void testSaveSupplier() {
        Supplier s = new Supplier();
        s.setName("Tech Corp");
        s.setEmail("sales@techcorp.com");

        Supplier saved = supplierRepository.save(s);

        Assertions.assertNotNull(saved.getId());
    }
}
