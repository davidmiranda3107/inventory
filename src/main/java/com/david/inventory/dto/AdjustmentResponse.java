package com.david.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdjustmentResponse {
    private Long id;
    private Long productId;
    private Integer quantity;

}
