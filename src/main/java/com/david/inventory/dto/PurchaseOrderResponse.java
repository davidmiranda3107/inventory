package com.david.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderResponse {

    private Long id;
    private Long supplierId;
    private LocalDateTime createdAt;
    private List<PurchaseOrderItemResponse> items;
}
