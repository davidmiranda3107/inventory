package com.david.inventory.service;

import com.david.inventory.dto.SupplierRequest;
import com.david.inventory.dto.SupplierResponse;
import com.david.inventory.exception.NotFoundException;
import com.david.inventory.model.Supplier;
import com.david.inventory.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository repo;

    public SupplierResponse create(SupplierRequest request) {
        Supplier supplier = Supplier.builder()
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .build();
        return toResponse(repo.save(supplier));
    }

    public SupplierResponse findById(Long id) {
        Supplier supplier = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier not found with id " + id));
        return toResponse(supplier);
    }

    public List<SupplierResponse> findAll() {
        return repo.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public SupplierResponse update(Long id, SupplierRequest data) {
        Optional<Supplier> existingSupplierOpt = repo.findById(id);

        if (existingSupplierOpt.isEmpty()) {
            throw new NotFoundException("Supplier not found with id: " + id);
        }

        Supplier existing = existingSupplierOpt.get();

        existing.setName(data.getName());
        existing.setEmail(data.getEmail());
        existing.setPhone(data.getPhone());
        existing.setAddress(data.getAddress());

        return toResponse(repo.save(existing));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Supplier not found with id " + id);
        }
        repo.deleteById(id);
    }

    private SupplierResponse toResponse(Supplier supplier) {
        return new SupplierResponse(supplier.getId(), supplier.getName(), supplier.getPhone(), supplier.getEmail(), supplier.getAddress());
    }
}
