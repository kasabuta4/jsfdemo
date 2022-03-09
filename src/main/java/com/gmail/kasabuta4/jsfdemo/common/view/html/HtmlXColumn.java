package com.gmail.kasabuta4.jsfdemo.common.view.html;

import java.util.function.Function;

public class HtmlXColumn<X, Y, E, V> {

  // required properties
  private final HtmlXYTable<X, Y, E> table;
  private final String header;
  private final Function<X, V> propertyGetter;

  // optional properties
  private Function<V, ?> converter = Function.identity();
  private boolean headerColumn = false;
  private String columnClass;
  private String headerCellClass;
  private String bodyCellClass;

  HtmlXColumn(HtmlXYTable<X, Y, E> table, String header, Function<X, V> propertyGetter) {
    this.table = table;
    this.header = header;
    this.propertyGetter = propertyGetter;
  }

  public HtmlXYTable<X, Y, E> endXColumn() {
    return table;
  }

  public HtmlXColumn<X, Y, E, V> converter(Function<V, ?> converter) {
    this.converter = converter;
    return this;
  }

  public HtmlXColumn<X, Y, E, V> headerColumn(boolean headerColumn) {
    this.headerColumn = headerColumn;
    return this;
  }

  public HtmlXColumn<X, Y, E, V> columnClass(String columnClass) {
    this.columnClass = columnClass;
    return this;
  }

  public HtmlXColumn<X, Y, E, V> headerCellClass(String headerCellClass) {
    this.headerCellClass = headerCellClass;
    return this;
  }

  public HtmlXColumn<X, Y, E, V> bodyCellClass(String bodyCellClass) {
    this.bodyCellClass = bodyCellClass;
    return this;
  }

  public String getHeader() {
    return header;
  }

  public Function<X, ?> getProperty() {
    return propertyGetter.andThen(converter);
  }

  public boolean isHeaderColumn() {
    return headerColumn;
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
}
