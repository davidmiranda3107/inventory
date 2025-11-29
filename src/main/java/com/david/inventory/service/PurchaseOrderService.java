package com.david.inventory.service;

import com.david.inventory.exception.NotFoundException;
import com.david.inventory.model.Product;
import com.david.inventory.model.PurchaseOrder;
import com.david.inventory.model.PurchaseOrderItem;
import com.david.inventory.model.StockLog;
import com.david.inventory.repository.ProductRepository;
import com.david.inventory.repository.PurchaseOrderItemRepository;
import com.david.inventory.repository.PurchaseOrderRepository;
import com.david.inventory.repository.StockLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class PurchaseOrderService {

    private final PurchaseOrderRepository orderRepo;
    private final PurchaseOrderItemRepository itemRepo;
    private final ProductRepository productRepo;
    private final StockLogRepository stockRepo;

    public PurchaseOrder create(PurchaseOrder order, List<PurchaseOrderItem> items) {

        order.setCreatedAt(LocalDateTime.now());
        PurchaseOrder savedOrder = orderRepo.save(order);

        for (PurchaseOrderItem item : items) {
            Product product = productRepo.findById(item.getProduct().getId())
                    .orElseThrow(() -> new NotFoundException("Product not found: " + item.getProduct().getId()));

            item.setOrder(savedOrder);
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

        return savedOrder;
    }

    public List<PurchaseOrder> list() {
        return orderRepo.findAll();
    }
}
