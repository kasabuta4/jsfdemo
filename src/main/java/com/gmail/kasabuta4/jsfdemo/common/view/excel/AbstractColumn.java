package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.util.List;
import java.util.Map;
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
  private Function<X, Object> highlightFunction;
  private Map<Object, List<String>> highlightStyleKeys;

  // temporary variables used during building workbook
  protected XSSFCellStyle headerStyle;
  protected List<XSSFCellStyle> bodyStyles;
  private Map<Object, List<XSSFCellStyle>> highlightStyles;

  protected AbstractColumn(
      T table, String header, Function<E, V> propertyGetter, ColumnWidth columnWidthConfigurator) {
    this.table = table;
    this.header = header;
    this.propertyGetter = propertyGetter;
    this.columnWidthConfigurator = columnWidthConfigurator;
  }

  protected abstract C self();

  public C highlight(Function<X, Object> func, Map<Object, List<String>> styleKeys) {
    this.highlightFunction = func;
    this.highlightStyleKeys = styleKeys;
    return self();
  }

  public C highlightStyleKeys(Map<Object, List<String>> highlightStyleKeys) {
    this.highlightStyleKeys = highlightStyleKeys;
    return self();
  }

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
        headerStyleKey != null
            ? getWorkbookModel().styleOf(headerStyleKey)
            : table.getHeaderStyle();
    bodyStyles =
        bodyStyleKeys != null
            ? bodyStyleKeys.stream().map(getWorkbookModel()::styleOf).collect(toList())
            : table.getBodyStyles();
    highlightStyles =
        highlightStyleKeys != null
            ? highlightStyleKeys.entrySet().stream()
                .collect(
                    toMap(
                        Map.Entry::getKey,
                        e ->
                            e.getValue().stream()
                                .map(getWorkbookModel()::styleOf)
                                .collect(toList())))
            : table.getHighlightStyles();
  }

  protected void writeHeader(XSSFSheet sheet, int rowIndex, int columnIndex, int headerRowCount) {
    XSSFCell cell = sheet.getRow(rowIndex).createCell(columnIndex);
    cell.setCellStyle(headerStyle);
    Cells.setCellValue(cell, header);
    Cells.mergeCell(cell, headerRowCount, 1);
  }

  protected XSSFCellStyle findStyle(X rowKey, int dataIndex) {
    List<XSSFCellStyle> styles;
    if (getHighlightFunction() != null && highlightStyles != null) {
      styles = highlightStyles.get(getHighlightFunction().apply(rowKey));
      if (styles == null) styles = bodyStyles;
    } else {
      styles = bodyStyles;
    }
    return styles == null ? null : styles.get(dataIndex % styles.size());
  }

  private Function<X, Object> getHighlightFunction() {
    return highlightFunction == null ? table.getHighlightFunction() : highlightFunction;
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
