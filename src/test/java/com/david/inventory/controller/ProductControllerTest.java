package com.david.inventory.controller;

import com.david.inventory.model.Category;
import com.david.inventory.model.Supplier;
import com.david.inventory.repository.CategoryRepository;
import com.david.inventory.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Test
    void testCreateProduct() throws Exception {
        Category c = new Category();
        c.setName("Food");
        Category category = categoryRepository.save(c);

        Supplier s = new Supplier();
        s.setName("Tech Corp");
        s.setEmail("sales@techcorp.com");

        Supplier supplier = supplierRepository.save(s);

        String json = """
            {
                "name": "Milk",
                "description": "Milk",
                "price": "1.50",
                "sku": "MLK001",
                "quantity": 10,
                "categoryId": %d,
                "supplierId": %d
            }
        """.formatted(category.getId(), supplier.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
