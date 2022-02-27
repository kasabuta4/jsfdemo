package com.gmail.kasabuta4.jsfdemo.common.poi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class XSSFCellUtil {

  public static void setCellValue(XSSFCell cell, Object value) {
    if (value == null) {
      cell.setBlank();
    } else if (value instanceof String) {
      String v = (String) value;
      cell.setCellValue(v);
    } else if (value instanceof Double) {
      Double v = (Double) value;
      cell.setCellValue(v);
    } else if (value instanceof Integer) {
      Integer v = (Integer) value;
      cell.setCellValue(v);
    } else if (value instanceof Long) {
      Long v = (Long) value;
      cell.setCellValue(v);
    } else if (value instanceof LocalDate) {
      LocalDate v = (LocalDate) value;
      cell.setCellValue(v);
    } else if (value instanceof YearMonth) {
      YearMonth v = (YearMonth) value;
      cell.setCellValue(v.atDay(1));
    } else if (value instanceof LocalDateTime) {
      LocalDateTime v = (LocalDateTime) value;
      cell.setCellValue(v);
    } else if (value instanceof Boolean) {
      Boolean v = (Boolean) value;
      cell.setCellValue(v);
    } else if (value instanceof Date) {
      Date v = (Date) value;
      cell.setCellValue(v);
    } else if (value instanceof Calendar) {
      Calendar v = (Calendar) value;
      cell.setCellValue(v);
    }
  }
}
