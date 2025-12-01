package com.david.inventory.service;

import com.david.inventory.dto.SaleOrderItemRequest;
import com.david.inventory.dto.SaleOrderItemResponse;
import com.david.inventory.dto.SaleOrderRequest;
import com.david.inventory.dto.SaleOrderResponse;
import com.david.inventory.exception.NotFoundException;
import com.david.inventory.model.*;
import com.david.inventory.repository.ProductRepository;
import com.david.inventory.repository.SaleOrderItemRepository;
import com.david.inventory.repository.SaleOrderRepository;
import com.david.inventory.repository.StockLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SaleOrderService {

    private ProductRepository productRepo;
    private StockLogRepository stockRepo;
    private SaleOrderRepository saleRepo;
    private SaleOrderItemRepository itemRepo;

    public SaleOrderResponse create(SaleOrderRequest saleRequest) {

        SaleOrder savedOrder = saleRepo.save(new SaleOrder());

        for (SaleOrderItemRequest itemRequest : saleRequest.getItems()) {
            Product product = productRepo.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found: " + itemRequest.getProductId()));

            SaleOrderItem item = SaleOrderItem.builder()
                    .order(savedOrder)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .build();
            itemRepo.save(item);

            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepo.save(product);

            StockLog log = new StockLog();
            log.setProduct(product);
            log.setQuantityChange(item.getQuantity());
            log.setNotes("Sale order #" + savedOrder.getId());
            log.setTimestamp(LocalDateTime.now());
            log.setType(StockLogType.STOCK_OUT);
            stockRepo.save(log);
        }

        return toResponse(savedOrder);
    }

    public SaleOrderResponse findById(Long id) {
        SaleOrder order = saleRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale Order not found with id " + id));
        return toResponse(order);
    }

    public List<SaleOrderResponse> findAll() {
        return saleRepo.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private SaleOrderResponse toResponse(SaleOrder order) {
        SaleOrderResponse response = new SaleOrderResponse();
        response.setId(order.getId());
        response.setCreatedAt(order.getCreatedAt());

        List<SaleOrderItemResponse> itemResponseList = new ArrayList<>();
        for (SaleOrderItem item : order.getItems()) {
            SaleOrderItemResponse itemResponse = new SaleOrderItemResponse();
            itemResponse.setId(item.getId());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setProductId(item.getProduct().getId());
        }

        response.setItems(itemResponseList);
        return response;
    }
}
