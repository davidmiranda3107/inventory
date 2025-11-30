package com.david.inventory.controller;

import com.david.inventory.dto.PurchaseOrderRequest;
import com.david.inventory.dto.PurchaseOrderResponse;
import com.david.inventory.service.PurchaseOrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@AllArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService service;

    @GetMapping
    public List<PurchaseOrderResponse> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public PurchaseOrderResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public PurchaseOrderResponse create(@Valid @RequestBody PurchaseOrderRequest request) {
        return service.create(request);
    }
}
