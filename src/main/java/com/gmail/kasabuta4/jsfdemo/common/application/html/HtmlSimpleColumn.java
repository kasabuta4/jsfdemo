package com.gmail.kasabuta4.jsfdemo.common.application.html;

import java.util.function.Function;

public class HtmlSimpleColumn<E, X, Y> {

  // required properties
  private final HtmlSimpleTable<E> table;
  private final String header;
  private final Function<X, Y> propertyGetter;

  // optional properties
  private Function<Y, ?> converter = Function.identity();
  private boolean headerColumn = false;
  private String columnClass;
  private String headerCellClass;
  private String bodyCellClass;

  HtmlSimpleColumn(HtmlSimpleTable<E> table, String header, Function<X, Y> propertyGetter) {
    this.table = table;
    this.header = header;
    this.propertyGetter = propertyGetter;
  }

  public HtmlSimpleTable<E> endSimpleColumn() {
    return table;
  }

  public HtmlSimpleColumn<E, X, Y> converter(Function<Y, ?> converter) {
    this.converter = converter;
    return this;
  }

  public HtmlSimpleColumn<E, X, Y> headerColumn(boolean headerColumn) {
    this.headerColumn = headerColumn;
    return this;
  }

  public HtmlSimpleColumn<E, X, Y> columnClass(String columnClass) {
    this.columnClass = columnClass;
    return this;
  }

  public HtmlSimpleColumn<E, X, Y> headerCellClass(String headerCellClass) {
    this.headerCellClass = headerCellClass;
    return this;
  }

  public HtmlSimpleColumn<E, X, Y> bodyCellClass(String bodyCellClass) {
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

  public Function<X, ?> getProperty() {
    return propertyGetter.andThen(converter);
  }
}
