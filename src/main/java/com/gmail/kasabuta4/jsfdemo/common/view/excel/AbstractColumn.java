package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import java.util.List;
import java.util.function.Function;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public abstract class AbstractColumn<C extends AbstractColumn, T extends AbstractTable, E, V, X> {

  // required properties
  protected final T table;
  protected final String header;
  private final Function<E, V> propertyGetter;
  private final ColumnWidth columnWidthConfigurator;

  // optional properties
  private Function<V, ?> converter = Function.identity();
  private String headerStyleKey;
  private List<String> bodyStyleKeys;
  private Function<X, Object> highlightPropertyGetter;
  private Function<Object, List<String>> highlightStyleKeyConverter;

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

  public C highlight(Function<X, Object> propertyGetter, Function<Object, List<String>> converter) {
    this.highlightPropertyGetter = propertyGetter;
    this.highlightStyleKeyConverter = converter;
    return self();
  }

  public C highlightStyleConverter(Function<Object, List<String>> converter) {
    this.highlightStyleKeyConverter = converter;
    return self();
  }

  protected void writeHeader(XSSFSheet sheet, int rowIndex, int columnIndex, int headerRowCount) {
    XSSFCell cell = sheet.getRow(rowIndex).createCell(columnIndex);
    XSSFCellStyle style =
        getHeaderStyleKey() == null ? null : getWorkbookModel().styleOf(getHeaderStyleKey());
    if (style != null) cell.setCellStyle(style);
    Cells.setCellValue(cell, header);
    Cells.mergeCell(cell, headerRowCount, 1);
  }

  protected XSSFCellStyle findStyle(X entity, int dataIndex) {
    List<String> styleKeys = highlightStyleKeys(entity);
    String styleKey = styleKeys == null ? null : styleKeys.get(dataIndex % styleKeys.size());
    return styleKey == null ? null : getWorkbookModel().styleOf(styleKey);
  }

  protected void configureColumnWidth(XSSFSheet worksheet, int columnIndex) {
    columnWidthConfigurator.configure(worksheet, columnIndex);
  }

  protected Object property(E entity) {
    return propertyGetter.andThen(converter).apply(entity);
  }

  List<String> highlightStyleKeys(X entity) {
    if (getHighlightPropertyGetter() == null || getHighlightStyleKeyConverter() == null)
      return getBodyStyleKeys();

    List<String> styleKeys =
        getHighlightPropertyGetter().andThen(getHighlightStyleKeyConverter()).apply(entity);
    return styleKeys == null ? getBodyStyleKeys() : styleKeys;
  }

  WorkbookModel getWorkbookModel() {
    return table.getWorkbookModel();
  }

  String getHeaderStyleKey() {
    return headerStyleKey == null ? table.getHeaderStyleKey() : headerStyleKey;
  }

  List<String> getBodyStyleKeys() {
    return bodyStyleKeys == null ? table.getBodyStyleKeys() : bodyStyleKeys;
  }

  Function<X, Object> getHighlightPropertyGetter() {
    return highlightPropertyGetter == null
        ? table.getHighlightPropertyGetter()
        : highlightPropertyGetter;
  }

  Function<Object, List<String>> getHighlightStyleKeyConverter() {
    return highlightStyleKeyConverter == null
        ? table.getHighlightStyleKeyConverter()
        : highlightStyleKeyConverter;
  }
}
