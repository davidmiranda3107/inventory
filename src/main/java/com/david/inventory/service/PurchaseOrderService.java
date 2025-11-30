package com.david.inventory.service;

import com.david.inventory.dto.PurchaseOrderItemRequest;
import com.david.inventory.dto.PurchaseOrderItemResponse;
import com.david.inventory.dto.PurchaseOrderRequest;
import com.david.inventory.dto.PurchaseOrderResponse;
import com.david.inventory.exception.NotFoundException;
import com.david.inventory.model.*;
import com.david.inventory.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository orderRepo;
    private final PurchaseOrderItemRepository itemRepo;
    private final ProductRepository productRepo;
    private final StockLogRepository stockRepo;
    private final SupplierRepository supplierRepo;

    public PurchaseOrderResponse create(PurchaseOrderRequest orderRequest) {

        Supplier supplier = mapSupplierIdToSupplier(orderRequest.getSupplierId());

        PurchaseOrder order = new PurchaseOrder();
        order.setSupplier(supplier);

        PurchaseOrder savedOrder = orderRepo.save(order);

        for (PurchaseOrderItemRequest itemRequest : orderRequest.getItems()) {
            Product product = productRepo.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found: " + itemRequest.getProductId()));

            PurchaseOrderItem item = PurchaseOrderItem.builder()
                            .order(savedOrder)
                            .product(product)
                            .quantity(itemRequest.getQuantity())
                            .build();
            itemRepo.save(item);

            product.setQuantity(product.getQuantity() + item.getQuantity());
            productRepo.save(product);

            StockLog log = new StockLog();
            log.setProduct(product);
            log.setQuantityChange(item.getQuantity());
            log.setNotes("Purchase order #" + savedOrder.getId());
            log.setTimestamp(LocalDateTime.now());
            stockRepo.save(log);
        }

        return toResponse(savedOrder);
    }

    public PurchaseOrderResponse findById(Long id) {
        PurchaseOrder order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Order not found with id " + id));
        return toResponse(order);
    }

    public List<PurchaseOrderResponse> findAll() {
        return orderRepo.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private Supplier mapSupplierIdToSupplier(Long supplierId) {
        if (supplierId == null) {
            return null;
        }

        return supplierRepo.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier not found with id: " + supplierId));
    }

    private PurchaseOrderResponse toResponse(PurchaseOrder order) {
        PurchaseOrderResponse response = new PurchaseOrderResponse();
        response.setId(order.getId());
        response.setCreatedAt(order.getCreatedAt());
        response.setSupplierId(order.getSupplier().getId());

        List<PurchaseOrderItemResponse> itemResponseList = new ArrayList<>();
        for(PurchaseOrderItem item : order.getItems()) {
            PurchaseOrderItemResponse itemResponse = new PurchaseOrderItemResponse();
            itemResponse.setId(item.getId());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setProductId(item.getProduct().getId());
            itemResponseList.add(itemResponse);
        }

        response.setItems(itemResponseList);

        return response;
    }
}
