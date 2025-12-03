package com.david.inventory.mapper;

import com.david.inventory.dto.ProductRequest;
import com.david.inventory.exception.NotFoundException;
import com.david.inventory.model.Category;
import com.david.inventory.model.Product;
import com.david.inventory.model.Supplier;
import com.david.inventory.repository.CategoryRepository;
import com.david.inventory.repository.SupplierRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Mapping(target = "category", source = "categoryId")
    @Mapping(target = "supplier", source = "supplierId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "minimumStock", ignore = true)
    public abstract Product toEntity(ProductRequest dto);

    @Mapping(target = "category", source = "categoryId")
    @Mapping(target = "supplier", source = "supplierId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "minimumStock", ignore = true)
    public abstract void updateEntityFromDto(ProductRequest dto, @MappingTarget Product entity);

    protected Category mapCategoryIdToCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }

        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Category not found with id: " + categoryId));
    }

    protected Supplier mapSupplierIdToSupplier(Long supplierId) {
        if (supplierId == null) {
            return null;
        }

        return supplierRepository.findById(supplierId)
                .orElseThrow(() -> new NotFoundException("Supplier not found with id: " + supplierId));
    }
}
