package com.gmail.kasabuta4.jsfdemo.common.view.html;

import java.util.function.Function;

public class HtmlSequenceColumn<T> {

  // required properties
  private final T table;
  private final String header;
  private final Function<Integer, Integer> propertyGetter = Function.identity();

  // optional properties
  private Function<Integer, ?> converter = Function.identity();
  private boolean headerColumn = false;
  private String columnClass;
  private String headerCellClass;
  private String bodyCellClass;

  public HtmlSequenceColumn(T table, String header) {
    this.table = table;
    this.header = header;
  }

  public T endSequenceColumn() {
    return table;
  }

  public HtmlSequenceColumn<T> converter(Function<Integer, ?> converter) {
    this.converter = converter;
    return this;
  }

  public HtmlSequenceColumn<T> headerColumn(boolean headerColumn) {
    this.headerColumn = headerColumn;
    return this;
  }

  public HtmlSequenceColumn<T> columnClass(String columnClass) {
    this.columnClass = columnClass;
    return this;
  }

  public HtmlSequenceColumn<T> headerCellClass(String headerCellClass) {
    this.headerCellClass = headerCellClass;
    return this;
  }

  public HtmlSequenceColumn<T> bodyCellClass(String bodyCellClass) {
    this.bodyCellClass = bodyCellClass;
    return this;
  }

  public boolean isHeaderColumn() {
    return headerColumn;
  }

  public String getHeader() {
    return header;
  }

  public String getColumnClass() {
    return columnClass;
  }

  public String getHeaderCellClass() {
    return headerCellClass;
  }

  public String getBodyCellClass() {
    return bodyCellClass;
  }

  public Function<Integer, ?> getProperty() {
    return propertyGetter.andThen(converter);
  }
}
