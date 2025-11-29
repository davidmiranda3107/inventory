package com.david.inventory.service;

import com.david.inventory.exception.NotFoundException;
import com.david.inventory.model.Supplier;
import com.david.inventory.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository repo;

    public Supplier create(Supplier supplier) {
        return repo.save(supplier);
    }

    public Supplier get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier not found with id " + id));
    }

    public List<Supplier> list() {
        return repo.findAll();
    }

    public Supplier update(Long id, Supplier data) {
        Supplier existing = get(id);
        existing.setName(data.getName());
        existing.setEmail(data.getEmail());
        existing.setPhone(data.getPhone());
        existing.setAddress(data.getAddress());
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Supplier not found with id " + id);
        }
        repo.deleteById(id);
    }
}
