package com.david.inventory.service;

import com.david.inventory.exception.NotFoundException;
import com.david.inventory.model.Product;
import com.david.inventory.model.StockLog;
import com.david.inventory.repository.ProductRepository;
import com.david.inventory.repository.StockLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;
    private final StockLogRepository stockRepo;

    public Product create(Product product) {
        return repo.save(product);
    }

    public Product get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id " + id));
    }

    public List<Product> list() {
        return repo.findAll();
    }

    public List<Product> findLowStock() {
        return repo.findByQuantityLessThanEqual(5);
    }

    public List<Product> searchByName(String name) {
        return repo.findByNameContainingIgnoreCase(name);
    }

    public void updateStock(Long id, int quantity, String reason) {
        Product product = get(id);

        product.setQuantity(product.getQuantity() + quantity);
        repo.save(product);

        StockLog log = new StockLog();
        log.setProduct(product);
        log.setQuantityChange(quantity);
        log.setNotes(reason);
        log.setTimestamp(LocalDateTime.now());
        stockRepo.save(log);
    }

    public Product update(Long id, Product data) {
        Product existing = get(id);
        existing.setName(data.getName());
        existing.setDescription(data.getDescription());
        existing.setPrice(data.getPrice());
        existing.setQuantity(data.getQuantity());
        existing.setCategory(data.getCategory());
        existing.setSupplier(data.getSupplier());
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Product not found with id " + id);
        }
        repo.deleteById(id);
    }
}
