package org.oj.server.util;

import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Excel implements AutoCloseable {

    private static final int MAX_ROW_SIZE = 1048570;

    private final SXSSFWorkbook workbook = new SXSSFWorkbook();

    public void setSheetHeader(String sheetName, List<String> headers) {
        SXSSFRow row = workbook.getSheet(sheetName).createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            row.createCell(i).setCellValue(headers.get(i));
        }
    }

    public void save(String excelPath) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(excelPath);
        workbook.write(outputStream);
        outputStream.close();
    }

    @Override
    public void close() throws IOException {
        workbook.close();
    }

    public void addSheet(String sheetName, List<String> headers, List<? extends ExcelExporter> collect) {
        workbook.createSheet(sheetName);

        if (collect.size() > MAX_ROW_SIZE) {
            workbook.removeSheetAt(workbook.getSheetIndex(sheetName));

            for (int current = 1; (current - 1) * MAX_ROW_SIZE < collect.size(); current++) {
                String newSheetName = sheetName + "_" + current;
                addSheet(newSheetName, headers, collect.subList(MAX_ROW_SIZE * (current - 1), Math.min(MAX_ROW_SIZE * current, collect.size())));
                current++;
            }
        } else {
            SXSSFSheet sheet = workbook.getSheet(sheetName);
            int rowIndex = 0;
            if (sheet.getRow(rowIndex) == null && headers != null) {
                setSheetHeader(sheetName, headers);
            }
            for (; rowIndex < collect.size(); rowIndex++) {
                SXSSFRow row = sheet.createRow(rowIndex + 1);
                collect.get(rowIndex).setRow(row, workbook);
            }
        }
    }
}
