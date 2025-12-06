package com.david.inventory.service;

import com.david.inventory.dto.ProductResponse;
import com.david.inventory.exception.InvalidExcelException;
import com.david.inventory.model.Category;
import com.david.inventory.model.Product;
import com.david.inventory.model.Supplier;
import com.david.inventory.repository.CategoryRepository;
import com.david.inventory.repository.ProductRepository;
import com.david.inventory.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImportService {

    private final CategoryRepository categoryRepo;
    private final SupplierRepository supplierRepo;
    private final ProductRepository productRepo;

    @Transactional
    public List<ProductResponse> importExcel(MultipartFile file) {

        List<String> errors = new ArrayList<>();
        List<Product> productsToSave = new ArrayList<>();
        List<ProductResponse> responses = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            int rowNumber = 0;
            for (Row row : sheet) {
                if (rowNumber == 0) { // skip header
                    rowNumber++;
                    continue;
                }

                try {
                    Product product = parseRow(row, rowNumber);
                    productsToSave.add(product);
                } catch (Exception e) {
                    errors.add("Row " + rowNumber + ": " + e.getMessage());
                }

                rowNumber++;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to process Excel file: " + e.getMessage());
        }

        if (!errors.isEmpty()) {
            throw new InvalidExcelException(errors);
        }

        // Save all products
        for (Product p : productsToSave) {
            Product saved = productRepo.save(p);
            responses.add(toResponse(saved));
        }

        return responses;
    }

    private Product parseRow(Row row, int rowNumber) {

        String name = readString(row, 0, true, "name");
        String description = readString(row, 1, false, "description");
        Double price = readDouble(row, 2, true, "price");
        Long categoryId = readLong(row, 3, true, "categoryId");
        Integer quantity = readInteger(row, 4, true, "quantity");
        Long supplierId = readLong(row, 5, true, "supplierId");
        String sku = readString(row, 6, true, "sku");

        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId));

        Supplier supplier = supplierRepo.findById(supplierId)
                .orElseThrow(() -> new RuntimeException("Supplier not found: " + supplierId));

        if (productRepo.existsBySku(sku)) {
            throw new RuntimeException("SKU already exists: " + sku);
        }

        return Product.builder()
                .name(name)
                .description(description)
                .price(price)
                .quantity(quantity)
                .sku(sku)
                .category(category)
                .supplier(supplier)
                .build();
    }

    private String readString(Row row, int index, boolean required, String field) {
        Cell cell = row.getCell(index);
        if (cell == null || cell.getStringCellValue().isBlank()) {
            if (required) throw new RuntimeException("Missing required field: " + field);
            return "";
        }
        return cell.getStringCellValue();
    }

    private Double readDouble(Row row, int index, boolean required, String field) {
        Cell cell = row.getCell(index);
        if (cell == null) {
            if (required) throw new RuntimeException("Missing required field: " + field);
            return null;
        }
        try {
            return cell.getNumericCellValue();
        } catch (Exception e) {
            throw new RuntimeException("Invalid number format in field: " + field);
        }
    }

    private Long readLong(Row row, int index, boolean required, String field) {
        Double value = readDouble(row, index, required, field);
        return value == null ? null : value.longValue();
    }

    private Integer readInteger(Row row, int index, boolean required, String field) {
        Double value = readDouble(row, index, required, field);
        return value == null ? null : value.intValue();
    }

    private ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getDescription(),
                p.getPrice(),
                p.getQuantity(),
                p.getCategory().getId(),
                p.getSupplier().getId(),
                p.getSku()
        );
    }
}
