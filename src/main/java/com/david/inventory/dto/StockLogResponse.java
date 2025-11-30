package com.david.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockLogResponse {

    private Long id;
    private Long productId;
    private Integer quantityChange;
    private LocalDateTime createdAt;
    private String operationType;
}
