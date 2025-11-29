package com.david.inventory.service;

import com.david.inventory.model.Product;
import com.david.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repo;

    public Product create(Product product) {
        return repo.save(product);
    }

    public List<Product> findAll() {
        return repo.findAll();
    }

    public List<Product> findLowStock() {
        return repo.findByQuantityLessThanEqual(5);
    }
}
