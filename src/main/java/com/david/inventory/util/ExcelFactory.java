package com.david.inventory.util;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExcelFactory {

    private static final String SHEET = "Products";
    public static final String TEMPLATE_FILENAME = "product_template.xlsx";
    public static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static byte[] createTemplateExcel() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(SHEET);

        addHeader(sheet);

        return buildMultipart(workbook, "product_template.xlsx");
    }

    private static void addHeader(Sheet sheet) {
        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("name");
        header.createCell(1).setCellValue("description");
        header.createCell(2).setCellValue("price");
        header.createCell(3).setCellValue("categoryId");
        header.createCell(4).setCellValue("quantity");
        header.createCell(5).setCellValue("supplierId");
        header.createCell(6).setCellValue("sku");
    }

    private static byte[] buildMultipart(Workbook workbook, String filename) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return out.toByteArray();
    }

}
