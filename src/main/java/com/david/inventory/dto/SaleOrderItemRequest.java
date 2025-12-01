package com.david.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SaleOrderItemRequest {
    @NotNull
    private Long productId;

    @NotNull
    @Positive
    private Integer quantity;
}
