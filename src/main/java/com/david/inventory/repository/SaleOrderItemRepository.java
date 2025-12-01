package com.david.inventory.repository;

import com.david.inventory.model.SaleOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleOrderItemRepository extends JpaRepository<SaleOrderItem, Long> {
}
