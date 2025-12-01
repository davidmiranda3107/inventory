package com.david.inventory.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class SaleOrderRequest {

    @NotNull
    @Size(min = 1)
    private List<SaleOrderItemRequest> items;
}
