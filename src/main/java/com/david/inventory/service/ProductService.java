package com.david.inventory.service;

import com.david.inventory.dto.ProductRequest;
import com.david.inventory.dto.ProductResponse;
import com.david.inventory.exception.NotFoundException;
import com.david.inventory.mapper.ProductMapper;
import com.david.inventory.model.Product;
import com.david.inventory.model.StockLog;
import com.david.inventory.repository.CategoryRepository;
import com.david.inventory.repository.ProductRepository;
import com.david.inventory.repository.StockLogRepository;
import com.david.inventory.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;
    private final StockLogRepository stockRepo;
    private final CategoryRepository categoryRepo;
    private final SupplierRepository supplierRepo;
    private final ProductMapper mapper;

    public ProductResponse create(ProductRequest request) {
        Product product = mapper.toEntity(request);
        return toResponse(repo.save(product));
    }

    public ProductResponse findById(Long id) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id " + id));
        return toResponse(product);
    }

    public List<ProductResponse> findAll() {
        return repo.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findLowStock() {

        return repo.findByQuantityLessThanEqual(5)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> searchByName(String name) {
        return repo.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public void updateStock(Long id, int quantity, String reason) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found with id " + id));

        product.setQuantity(product.getQuantity() + quantity);
        repo.save(product);

        StockLog log = new StockLog();
        log.setProduct(product);
        log.setQuantityChange(quantity);
        log.setNotes(reason);
        log.setTimestamp(LocalDateTime.now());
        stockRepo.save(log);
    }

    public ProductResponse update(Long id, ProductRequest data) {
        Optional<Product> existingProductOpt  = repo.findById(id);

        if (existingProductOpt.isEmpty()) {
            throw new NotFoundException("Product not found with id: " + id);
        }

        Product existing = existingProductOpt.get();

        mapper.updateEntityFromDto(data, existing);

        return toResponse(repo.save(existing));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Product not found with id " + id);
        }
        repo.deleteById(id);
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(),
                product.getPrice(), product.getQuantity(), product.getCategory().getId(),
                product.getSupplier().getId());
    }
}
