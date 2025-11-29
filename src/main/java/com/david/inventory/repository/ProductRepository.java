package com.david.inventory.repository;

import com.david.inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByQuantityLessThanEqual(int quantity);

    List<Product> findByNameContainingIgnoreCase(String name);
}
