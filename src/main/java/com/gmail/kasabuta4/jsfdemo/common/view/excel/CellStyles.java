package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CellStyles {
  public static XSSFCellStyle create(XSSFWorkbook workbook, XSSFFont font) {
    XSSFCellStyle style = workbook.createCellStyle();
    style.setFont(font);
    return style;
  }

  public static XSSFCellStyle create(XSSFWorkbook workbook, String numberFormat) {
    XSSFCellStyle style = workbook.createCellStyle();
    style.setDataFormat(workbook.createDataFormat().getFormat(numberFormat));
    return style;
  }

  public static XSSFCellStyle create(XSSFWorkbook workbook, XSSFColor fillColor) {
    XSSFCellStyle style = workbook.createCellStyle();
    style.setFillForegroundColor(fillColor);
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    return style;
  }

  public static XSSFCellStyle create(XSSFWorkbook workbook, XSSFFont font, XSSFColor fillColor) {
    XSSFCellStyle style = workbook.createCellStyle();
    style.setFont(font);
    style.setFillForegroundColor(fillColor);
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    return style;
  }

  public static XSSFCellStyle create(
      XSSFWorkbook workbook, String numberFormat, XSSFColor fillColor) {
    XSSFCellStyle style = workbook.createCellStyle();
    style.setDataFormat(workbook.createDataFormat().getFormat(numberFormat));
    style.setFillForegroundColor(fillColor);
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    return style;
  }

  public static XSSFCellStyle create(XSSFWorkbook workbook, String numberFormat, XSSFFont font) {
    XSSFCellStyle style = workbook.createCellStyle();
    style.setDataFormat(workbook.createDataFormat().getFormat(numberFormat));
    style.setFont(font);
    return style;
  }

  public static XSSFCellStyle create(
      XSSFWorkbook workbook, String numberFormat, XSSFFont font, XSSFColor fillColor) {
    XSSFCellStyle style = workbook.createCellStyle();
    style.setDataFormat(workbook.createDataFormat().getFormat(numberFormat));
    style.setFont(font);
    style.setFillForegroundColor(fillColor);
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    return style;
  }
}
