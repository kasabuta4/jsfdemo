package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import java.util.List;
import java.util.function.Function;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class AbstractColumn<C extends AbstractColumn, T extends WorkSheetModel, E, V> {

  // required properties
  private final T table;
  protected final String header;
  protected final Function<E, V> propertyGetter;
  private final ColumnWidthConfigurator columnWidthConfigurator;
  private final NumberFormat format;

  // optional properties
  protected Function<V, ?> converter = Function.identity();
  private Stylers.Simple headerStyler = Stylers.forTableHeader();
  private Stylers.FormattableList bodyStyler = Stylers.forFormattableTableBody();

  // temporary variables used during building workbook
  protected XSSFCellStyle headerStyle;
  protected List<XSSFCellStyle> bodyStyles;

  protected AbstractColumn(
      T table,
      String header,
      Function<E, V> propertyGetter,
      ColumnWidthConfigurator columnWidthConfigurator,
      NumberFormat format) {
    this.table = table;
    this.header = header;
    this.propertyGetter = propertyGetter;
    this.columnWidthConfigurator = columnWidthConfigurator;
    this.format = format;
  }

  protected abstract C self();

  public T endColumn() {
    return table;
  }

  public C converter(Function<V, ?> converter) {
    this.converter = converter;
    return self();
  }

  public C headerStyler(Stylers.Simple headerStyler) {
    this.headerStyler = headerStyler;
    return self();
  }

  public C bodyStyler(Stylers.FormattableList bodyStyler) {
    this.bodyStyler = bodyStyler;
    return self();
  }

  protected void initStyles(XSSFWorkbook workbook) {
    headerStyle = headerStyler.createStyle(workbook);
    bodyStyles = bodyStyler.createStyles(workbook, format);
  }

  protected void writeHeader(XSSFSheet sheet, int rowIndex, int columnIndex, int headerRowCount) {
    XSSFCell cell = sheet.getRow(rowIndex).createCell(columnIndex);
    cell.setCellStyle(headerStyle);
    XSSFCellUtil.setCellValue(cell, header);
    XSSFCellUtil.mergeCell(cell, headerRowCount, 1);
  }

  protected void writeRecord(
      E entity, int dataIndex, XSSFSheet sheet, int rowIndex, int columnIndex) {
    XSSFCell cell = sheet.getRow(rowIndex).createCell(columnIndex);
    cell.setCellStyle(bodyStyles.get(dataIndex % bodyStyles.size()));
    XSSFCellUtil.setCellValue(cell, propertyGetter.andThen(converter).apply(entity));
  }

  protected void configureColumnWidth(XSSFSheet worksheet, int columnIndex) {
    columnWidthConfigurator.configure(worksheet, columnIndex);
  }
}
