package com.david.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleOrderResponse {
    private Long id;
    private LocalDateTime createdAt;
    private List<SaleOrderItemResponse> items;
}
