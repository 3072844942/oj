package org.oj.server.util;

import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

public interface ExcelExporter {
    void setRow(SXSSFRow row, SXSSFWorkbook workbook);
}
