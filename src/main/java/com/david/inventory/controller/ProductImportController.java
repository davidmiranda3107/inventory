package com.david.inventory.controller;

import com.david.inventory.dto.ProductResponse;
import com.david.inventory.exception.InvalidExcelException;
import com.david.inventory.service.ProductImportService;
import com.david.inventory.util.ExcelFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products/import")
@RequiredArgsConstructor
public class ProductImportController {

    private final ProductImportService service;

    @PostMapping("/excel")
    public ResponseEntity<?> importExcel(@RequestParam("file")MultipartFile file) {
        try {
            List<ProductResponse> imported = service.importExcel(file);
            return ResponseEntity.ok(imported);
        } catch (InvalidExcelException e) {
            return ResponseEntity.badRequest().body(e.getErrors());
        }
    }

    @GetMapping("/excel/template")
    public ResponseEntity<byte[]> downloadTemplate() throws IOException {
        byte[] excelBytes = ExcelFactory.createTemplateExcel();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(ExcelFactory.CONTENT_TYPE));
        headers.setContentDispositionFormData("attachment", ExcelFactory.TEMPLATE_FILENAME);

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelBytes);
    }
}
