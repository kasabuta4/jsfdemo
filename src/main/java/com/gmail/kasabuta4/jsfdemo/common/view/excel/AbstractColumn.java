package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Function;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public abstract class AbstractColumn<C extends AbstractColumn, T extends AbstractTable, E, V> {

  // required properties
  private final T table;
  protected final String header;
  private final Function<E, V> propertyGetter;
  private final ColumnWidth columnWidthConfigurator;

  // optional properties
  private Function<V, ?> converter = Function.identity();
  private String headerStyleKey;
  private List<String> bodyStyleKeys;

  // temporary variables used during building workbook
  protected XSSFCellStyle headerStyle;
  protected List<XSSFCellStyle> bodyStyles;

  protected AbstractColumn(
      T table, String header, Function<E, V> propertyGetter, ColumnWidth columnWidthConfigurator) {
    this.table = table;
    this.header = header;
    this.propertyGetter = propertyGetter;
    this.columnWidthConfigurator = columnWidthConfigurator;
  }

  protected abstract C self();

  public T endColumn() {
    return table;
  }

  public C converter(Function<V, ?> converter) {
    this.converter = converter;
    return self();
  }

  public C headerStyleKey(String headerStyleKey) {
    this.headerStyleKey = headerStyleKey;
    return self();
  }

  public C bodyStyleKeys(List<String> bodyStyleKeys) {
    this.bodyStyleKeys = bodyStyleKeys;
    return self();
  }

  protected void initStyles() {
    headerStyle =
        headerStyleKey != null ? getWorkbookModel().styleOf(headerStyleKey) : table.headerStyle;
    bodyStyles =
        bodyStyleKeys != null
            ? bodyStyleKeys.stream().map(getWorkbookModel()::styleOf).collect(toList())
            : table.bodyStyles;
  }

  protected void writeHeader(XSSFSheet sheet, int rowIndex, int columnIndex, int headerRowCount) {
    XSSFCell cell = sheet.getRow(rowIndex).createCell(columnIndex);
    cell.setCellStyle(headerStyle);
    Cells.setCellValue(cell, header);
    Cells.mergeCell(cell, headerRowCount, 1);
  }

  protected void writeRecord(
      E entity, int dataIndex, XSSFSheet sheet, int rowIndex, int columnIndex) {
    XSSFCell cell = sheet.getRow(rowIndex).createCell(columnIndex);
    cell.setCellStyle(bodyStyles.get(dataIndex % bodyStyles.size()));
    Cells.setCellValue(cell, property(entity));
  }

  protected void configureColumnWidth(XSSFSheet worksheet, int columnIndex) {
    columnWidthConfigurator.configure(worksheet, columnIndex);
  }

  protected Object property(E entity) {
    return propertyGetter.andThen(converter).apply(entity);
  }

  protected WorkbookModel getWorkbookModel() {
    return table.getWorkbookModel();
  }
}
