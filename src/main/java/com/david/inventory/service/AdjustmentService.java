package com.david.inventory.service;

import com.david.inventory.dto.AdjustmentRequest;
import com.david.inventory.dto.AdjustmentResponse;
import com.david.inventory.exception.NotFoundException;
import com.david.inventory.model.Product;
import com.david.inventory.model.StockLog;
import com.david.inventory.model.StockLogType;
import com.david.inventory.repository.ProductRepository;
import com.david.inventory.repository.StockLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AdjustmentService {

    private final ProductRepository productRepo;
    private final StockLogRepository stockRepo;

    public AdjustmentResponse create(AdjustmentRequest request) {
        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found: " + request.getProductId()));

        product.setQuantity(product.getQuantity() + request.getQuantity());
        productRepo.save(product);

        StockLog log = new StockLog();
        log.setProduct(product);
        log.setQuantityChange(request.getQuantity());
        log.setNotes(request.getReason());
        log.setTimestamp(LocalDateTime.now());
        log.setType(StockLogType.ADJUSTMENT);
        stockRepo.save(log);

        return toResponse(log.getId(), product.getId(), product.getQuantity());

    }

    private AdjustmentResponse toResponse(Long id, Long productId, Integer quantity) {

        return AdjustmentResponse.builder()
                .id(id)
                .productId(productId)
                .quantity(quantity)
                .build();
    }

}
