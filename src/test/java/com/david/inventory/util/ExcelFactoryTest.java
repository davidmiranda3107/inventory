package com.david.inventory.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ExcelFactoryTest {

    private static final String SHEET = "Products";

    public static MultipartFile createValidExcel(Long categoryId, Long supplierId) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(SHEET);

        addHeader(sheet);

        Row r1 = sheet.createRow(1);
        r1.createCell(0).setCellValue("Keyboard");      //product name
        r1.createCell(1).setCellValue("Mechanical");    //description
        r1.createCell(2).setCellValue(49.99);           //price
        r1.createCell(3).setCellValue(categoryId);      //category
        r1.createCell(4).setCellValue(100);             //quantity
        r1.createCell(5).setCellValue(supplierId);      //supplier
        r1.createCell(6).setCellValue("KB001");         //sku

        Row r2 = sheet.createRow(2);
        r2.createCell(0).setCellValue("Mouse");
        r2.createCell(1).setCellValue("Wireless");
        r2.createCell(2).setCellValue(19.99);
        r2.createCell(3).setCellValue(categoryId);
        r2.createCell(4).setCellValue(50);
        r2.createCell(5).setCellValue(supplierId);
        r2.createCell(6).setCellValue("MS009");

        return buildMultipart(workbook, "valid.xlsx");
    }

    public static MultipartFile createExcelMissingField() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(SHEET);

        addHeader(sheet);

        Row r1 = sheet.createRow(1);
        r1.createCell(0).setCellValue("");   // name missing
        r1.createCell(1).setCellValue("desc");
        r1.createCell(2).setCellValue(10.0);
        r1.createCell(3).setCellValue(1);
        r1.createCell(4).setCellValue(50);
        r1.createCell(5).setCellValue(1);
        r1.createCell(6).setCellValue("SKU123");

        return buildMultipart(workbook, "missing_field.xlsx");
    }

    public static MultipartFile createExcelInvalidFormat() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(SHEET);

        addHeader(sheet);

        Row r1 = sheet.createRow(1);
        r1.createCell(0).setCellValue("Keyboard");
        r1.createCell(1).setCellValue("desc");

        Cell priceCell = r1.createCell(2);
        priceCell.setCellValue("abc"); // invalid number

        r1.createCell(3).setCellValue(1);
        r1.createCell(4).setCellValue(100);
        r1.createCell(5).setCellValue(1);
        r1.createCell(6).setCellValue("KB001");

        return buildMultipart(workbook, "invalid_format.xlsx");
    }

    public static MultipartFile createExcelDuplicateSku() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(SHEET);

        addHeader(sheet);

        Row r1 = sheet.createRow(1);
        r1.createCell(0).setCellValue("Keyboard");
        r1.createCell(1).setCellValue("desc");
        r1.createCell(2).setCellValue(49.99);
        r1.createCell(3).setCellValue(1);
        r1.createCell(4).setCellValue(100);
        r1.createCell(5).setCellValue(1);
        r1.createCell(6).setCellValue("SKU1"); // Already existing SKU

        return buildMultipart(workbook, "duplicate_sku.xlsx");
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

    private static MultipartFile buildMultipart(Workbook workbook, String filename) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new MockMultipartFile(
                "file",
                filename,
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                out.toByteArray()
        );
    }
}
