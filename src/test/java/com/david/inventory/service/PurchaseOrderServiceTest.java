package com.david.inventory.service;

import com.david.inventory.dto.PurchaseOrderItemRequest;
import com.david.inventory.dto.PurchaseOrderRequest;
import com.david.inventory.dto.PurchaseOrderResponse;
import com.david.inventory.model.Category;
import com.david.inventory.model.Product;
import com.david.inventory.model.Supplier;
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
public class PurchaseOrderServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Test
    void testCreatePurchaseOrder() {
        Category c = new Category();
        c.setName("Hardware");
        categoryRepository.save(c);

        Product p = new Product();
        p.setName("Keyboard");
        p.setSku("KB001");
        p.setQuantity(5);
        p.setCategory(c);

        Product product = productRepository.save(p);

        Supplier s = new Supplier();
        s.setName("Tech Corp");
        s.setEmail("sales@techcorp.com");

        Supplier supplier = supplierRepository.save(s);

        PurchaseOrderRequest orderRequest = new PurchaseOrderRequest();
        orderRequest.setSupplierId(supplier.getId());
        PurchaseOrderItemRequest itemRequest = new PurchaseOrderItemRequest();
        itemRequest.setProductId(product.getId());
        itemRequest.setQuantity(5);
        List<PurchaseOrderItemRequest> itemRequestList = new ArrayList<>();
        itemRequestList.add(itemRequest);
        orderRequest.setItems(itemRequestList);

        PurchaseOrderResponse response = purchaseOrderService.create(orderRequest);

        Assertions.assertNotNull(response.getId());



    }
}
