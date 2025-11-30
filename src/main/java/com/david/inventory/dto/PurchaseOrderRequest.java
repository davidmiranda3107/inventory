package com.david.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class PurchaseOrderRequest {

    @NotNull
    private Long supplierId;

    @NotNull
    @Size(min = 1)
    private List<PurchaseOrderItemRequest> items;
}
