package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import java.util.List;
import java.util.function.Function;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SimpleColumn<E, X, Y> {

  // required properties
  private final SimpleTable<E> table;
  private final String header;
  private final ColumnWidthConfigurator columnWidthConfigurator;
  private final NumberFormat format;
  private final Function<X, Y> propertyGetter;

  // optional properties
  private Function<Y, ?> converter = Function.identity();
  private Stylers.Simple headerStyler = Stylers.forTableHeader();
  private Stylers.FormattableList bodyStyler = Stylers.forFormattableTableBody();

  // temporary variables used during building workbook
  private XSSFCellStyle headerStyle;
  private List<XSSFCellStyle> bodyStyles;

  SimpleColumn(
      SimpleTable<E> table,
      String header,
      Function<X, Y> property,
      ColumnWidthConfigurator columnWidthConfigurator,
      NumberFormat format) {
    this.table = table;
    this.header = header;
    this.propertyGetter = property;
    this.columnWidthConfigurator = columnWidthConfigurator;
    this.format = format;
  }

  public SimpleTable<E> endSimpleColumn() {
    return table;
  }

  public SimpleColumn<E, X, Y> converter(Function<Y, ?> converter) {
    this.converter = converter;
    return this;
  }

  public SimpleColumn<E, X, Y> headerStyler(Stylers.Simple headerStyler) {
    this.headerStyler = headerStyler;
    return this;
  }

  public SimpleColumn<E, X, Y> bodyStyler(Stylers.FormattableList bodyStyler) {
    this.bodyStyler = bodyStyler;
    return this;
  }

  void initStyles(XSSFWorkbook workbook) {
    headerStyle = headerStyler.createStyle(workbook);
    bodyStyles = bodyStyler.createStyles(workbook, format);
  }

  void writeHeader(XSSFCell cell) {
    cell.setCellStyle(headerStyle);
    XSSFCellUtil.setCellValue(cell, header);
  }

  void writeBody(X entity, XSSFCell cell, int rowIndex) {
    cell.setCellStyle(bodyStyles.get(rowIndex % bodyStyles.size()));
    XSSFCellUtil.setCellValue(cell, propertyGetter.andThen(converter).apply(entity));
  }

  void configureColumnWidth(XSSFSheet worksheet, int columnIndex) {
    columnWidthConfigurator.configure(worksheet, columnIndex);
  }
}
