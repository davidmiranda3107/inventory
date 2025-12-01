package com.david.inventory.controller;

import com.david.inventory.dto.SaleOrderRequest;
import com.david.inventory.dto.SaleOrderResponse;
import com.david.inventory.service.SaleOrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sale-orders")
@AllArgsConstructor
public class SaleOrderController {

    private final SaleOrderService service;

    @GetMapping
    public List<SaleOrderResponse> list() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public SaleOrderResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public SaleOrderResponse create(@Valid @RequestBody SaleOrderRequest request) {
        return service.create(request);
    }
}
