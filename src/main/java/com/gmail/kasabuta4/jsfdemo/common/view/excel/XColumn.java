package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import java.util.List;
import java.util.function.Function;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XColumn<X, Y, E, V> {

  // required properties
  private final XYTable<X, Y, E> table;
  private final String header;
  private final Function<X, V> propertyGetter;
  private final int characters;
  private final NumberFormat format;

  // optional properties
  private Function<V, ?> converter = Function.identity();
  private Stylers.Simple headerStyler = Stylers.forTableHeader();
  private Stylers.FormattableList bodyStyler = Stylers.forFormattableTableBody();

  // temporary variables used during building workbook
  private XSSFCellStyle headerStyle;
  private List<XSSFCellStyle> bodyStyles;

  XColumn(
      XYTable<X, Y, E> table,
      String header,
      Function<X, V> propertyGetter,
      int characters,
      NumberFormat format) {
    this.table = table;
    this.header = header;
    this.propertyGetter = propertyGetter;
    this.characters = characters;
    this.format = format;
  }

  public XYTable<X, Y, E> endXColumn() {
    return table;
  }

  public XColumn<X, Y, E, V> converter(Function<V, ?> converter) {
    this.converter = converter;
    return this;
  }

  public XColumn<X, Y, E, V> headerStyler(Stylers.Simple headerStyler) {
    this.headerStyler = headerStyler;
    return this;
  }

  public XColumn<X, Y, E, V> bodyStyler(Stylers.FormattableList bodyStyler) {
    this.bodyStyler = bodyStyler;
    return this;
  }

  void initStyles(XSSFWorkbook workbook) {
    headerStyle = headerStyler.createStyle(workbook);
    bodyStyles = bodyStyler.createStyles(workbook, format);
  }

  void writeHeader(XSSFSheet sheet, int rowIndex, int columnIndex, int maxColumnTitles) {
    XSSFRow row = sheet.getRow(rowIndex);
    XSSFCell cell = row.createCell(columnIndex);
    cell.setCellStyle(headerStyle);
    XSSFCellUtil.setCellValue(cell, header);
    if (maxColumnTitles > 0)
      sheet.addMergedRegion(
          new CellRangeAddress(rowIndex, rowIndex + maxColumnTitles, columnIndex, columnIndex));
  }

  void writeBody(X x, XSSFCell cell, int rowIndex) {
    cell.setCellStyle(bodyStyles.get(rowIndex % bodyStyles.size()));
    XSSFCellUtil.setCellValue(cell, propertyGetter.andThen(converter).apply(x));
  }

  int getColumnWidth() {
    return (characters + 1) * 256;
  }
}
