package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Fonts {
  public static XSSFFont ofSize(XSSFWorkbook workbook, int point) {
    XSSFFont font = workbook.createFont();
    resetToStandardFont(workbook, font);
    font.setFontHeightInPoints((short) point);
    return font;
  }

  public static XSSFFont bold(XSSFWorkbook workbook) {
    XSSFFont font = workbook.createFont();
    resetToStandardFont(workbook, font);
    font.setBold(true);
    return font;
  }

  public static XSSFFont boldOfSize(XSSFWorkbook workbook, int point) {
    XSSFFont font = workbook.createFont();
    resetToStandardFont(workbook, font);
    font.setBold(true);
    font.setFontHeightInPoints((short) point);
    return font;
  }

  public static XSSFFont ofColor(XSSFWorkbook workbook, XSSFColor color) {
    XSSFFont font = workbook.createFont();
    resetToStandardFont(workbook, font);
    font.setColor(color);
    return font;
  }

  private static void resetToStandardFont(XSSFWorkbook workbook, XSSFFont font) {
    XSSFFont standardFont = workbook.getStylesSource().getStyleAt(0).getFont();
    font.setBold(standardFont.getBold());
    font.setColor(standardFont.getXSSFColor());
    font.setFontHeight(standardFont.getFontHeight());
    font.setFontName(standardFont.getFontName());
    font.setItalic(standardFont.getItalic());
    font.setStrikeout(standardFont.getStrikeout());
    font.setTypeOffset(standardFont.getTypeOffset());
    font.setUnderline(standardFont.getUnderline());
  }
}
