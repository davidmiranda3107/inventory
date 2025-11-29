package com.david.inventory.service;

import com.david.inventory.exception.NotFoundException;
import com.david.inventory.model.Category;
import com.david.inventory.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository repo;

    public Category create(Category category) {
        return repo.save(category);
    }

    public Category get(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found with id " + id));
    }

    public List<Category> list() {
        return repo.findAll();
    }

    public Category update(Long id, Category data) {
        Category existing = get(id);
        existing.setName(data.getName());
        existing.setDescription(data.getDescription());
        return repo.save(existing);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Category not found with id " + id);
        }
        repo.deleteById(id);
    }
}
