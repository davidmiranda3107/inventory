package com.david.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleOrderItemResponse {
    private Long id;
    private Long productId;
    private Integer quantity;
}
