package com.david.inventory.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdjustmentRequest {

    @NotNull
    private Long productId;

    @NotNull
    private Integer quantity;

    @NotNull
    private String reason;
}
