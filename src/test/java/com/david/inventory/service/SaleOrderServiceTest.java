package com.david.inventory.service;

import com.david.inventory.dto.SaleOrderItemRequest;
import com.david.inventory.dto.SaleOrderRequest;
import com.david.inventory.dto.SaleOrderResponse;
import com.david.inventory.model.Category;
import com.david.inventory.model.Product;
import com.david.inventory.repository.CategoryRepository;
import com.david.inventory.repository.ProductRepository;
import com.david.inventory.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class SaleOrderServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SaleOrderService saleOrderService;

    @Test
    void testCreateSaleOrder() {
        Category c = new Category();
        c.setName("Hardware");
        categoryRepository.save(c);

        Product p = new Product();
        p.setName("Keyboard");
        p.setSku("KB001");
        p.setQuantity(5);
        p.setCategory(c);

        Product product = productRepository.save(p);

        SaleOrderRequest saleOrderRequest = new SaleOrderRequest();
        SaleOrderItemRequest itemRequest = new SaleOrderItemRequest();
        itemRequest.setProductId(product.getId());
        itemRequest.setQuantity(5);
        List<SaleOrderItemRequest> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequest);
        saleOrderRequest.setItems(itemRequestList);

        SaleOrderResponse response = saleOrderService.create(saleOrderRequest);

        Assertions.assertNotNull(response.getId());
    }
}
