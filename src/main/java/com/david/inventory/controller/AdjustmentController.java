package com.david.inventory.controller;

import com.david.inventory.dto.AdjustmentRequest;
import com.david.inventory.dto.AdjustmentResponse;
import com.david.inventory.service.AdjustmentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/adjustments")
@AllArgsConstructor
public class AdjustmentController {

    private final AdjustmentService service;

    @PostMapping
    public AdjustmentResponse create(@Valid @RequestBody AdjustmentRequest request) {
        return service.create(request);
    }
}
