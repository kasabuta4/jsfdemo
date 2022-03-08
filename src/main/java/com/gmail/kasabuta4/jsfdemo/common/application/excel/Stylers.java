package com.gmail.kasabuta4.jsfdemo.common.application.excel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Stylers {

  private static final Color FILL_COLOR_FOR_EVEN_ROW = new Color(224, 255, 255);

  public static interface Simple {
    XSSFCellStyle createStyle(XSSFWorkbook workbook);
  }

  public static interface Formattable {
    XSSFCellStyle createStyle(XSSFWorkbook workbook, NumberFormat format);
  }

  public static interface FormattableList {
    List<XSSFCellStyle> createStyles(XSSFWorkbook workbook, NumberFormat format);
  }

  public static Simple forTableCaption() {
    return Stylers::createTableCaptionStyle;
  }

  public static Simple forTableHeader() {
    return Stylers::createTableHeaderStyle;
  }

  public static FormattableList forFormattableTableBody() {
    return Stylers::createFormattableTableBodyStyles;
  }

  public static Formattable forFormattableTableHeader() {
    return Stylers::createFormattableTableHeaderStyle;
  }

  private static XSSFCellStyle createTableCaptionStyle(XSSFWorkbook workbook) {
    XSSFFont standardFont = workbook.getStylesSource().getFontAt(0);

    XSSFFont font = workbook.createFont();
    font.setBold(true);
    font.setColor(standardFont.getColor());
    font.setFontName(standardFont.getFontName());
    font.setFontHeightInPoints((short) (standardFont.getFontHeightInPoints() + 4));

    XSSFCellStyle style = workbook.createCellStyle();
    style.setFont(font);
    return style;
  }

  private static XSSFCellStyle createTableHeaderStyle(XSSFWorkbook workbook) {
    XSSFFont standardFont = workbook.getStylesSource().getFontAt(0);

    XSSFFont font = workbook.createFont();
    font.setBold(true);
    font.setColor(standardFont.getColor());
    font.setFontName(standardFont.getFontName());
    font.setFontHeightInPoints(standardFont.getFontHeightInPoints());

    XSSFCellStyle style = workbook.createCellStyle();
    style.setFont(font);
    return style;
  }

  private static List<XSSFCellStyle> createFormattableTableBodyStyles(
      XSSFWorkbook workbook, NumberFormat format) {
    final XSSFColor fillColor =
        new XSSFColor(FILL_COLOR_FOR_EVEN_ROW, workbook.getStylesSource().getIndexedColors());

    XSSFCellStyle oddStyle = workbook.createCellStyle();
    oddStyle.setFillPattern(FillPatternType.NO_FILL);
    oddStyle.setDataFormat(format.getFormat(workbook));

    XSSFCellStyle evenStyle = workbook.createCellStyle();
    evenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    evenStyle.setFillForegroundColor(fillColor);
    evenStyle.setDataFormat(format.getFormat(workbook));

    List<XSSFCellStyle> styles = new ArrayList<>(2);
    styles.add(oddStyle);
    styles.add(evenStyle);
    return Collections.unmodifiableList(styles);
  }

  private static XSSFCellStyle createFormattableTableHeaderStyle(
      XSSFWorkbook workbook, NumberFormat format) {
    XSSFCellStyle style = workbook.createCellStyle();
    style.setDataFormat(format.getFormat(workbook));
    return style;
  }
}
