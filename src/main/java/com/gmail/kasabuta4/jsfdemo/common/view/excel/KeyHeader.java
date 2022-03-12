package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import java.util.function.Function;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class KeyHeader<MapColumn, Y, P> {

  // required properties
  private final MapColumn mapColumn;
  private final Function<Y, P> propertyGetter;
  private final NumberFormat format;

  // optional properties
  private Function<P, ?> converter = Function.identity();
  private Stylers.Formattable styler = Stylers.forFormattableTableHeader();

  // temporary variables used during building workbook
  private XSSFCellStyle style;

  KeyHeader(MapColumn mapColumn, Function<Y, P> propertyGetter, NumberFormat format) {
    this.mapColumn = mapColumn;
    this.propertyGetter = propertyGetter;
    this.format = format;
  }

  public MapColumn endKeyHeader() {
    return mapColumn;
  }

  public KeyHeader<MapColumn, Y, P> converter(Function<P, ?> converter) {
    this.converter = converter;
    return this;
  }

  public KeyHeader<MapColumn, Y, P> styler(Stylers.Formattable styler) {
    this.styler = styler;
    return this;
  }

  void initStyles(XSSFWorkbook workbook) {
    style = styler.createStyle(workbook, format);
  }

  void writeHeader(XSSFCell cell, Y y) {
    cell.setCellStyle(style);
    XSSFCellUtil.setCellValue(cell, propertyGetter.andThen(converter).apply(y));
  }

  void writeKeyHeader(Y y, XSSFSheet sheet, int rowIndex, int columnIndex) {
    XSSFCell cell = sheet.getRow(rowIndex).createCell(columnIndex);
    cell.setCellStyle(style);
    XSSFCellUtil.setCellValue(cell, propertyGetter.andThen(converter).apply(y));
  }
}
