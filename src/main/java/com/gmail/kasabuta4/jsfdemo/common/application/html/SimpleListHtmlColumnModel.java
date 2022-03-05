package com.gmail.kasabuta4.jsfdemo.common.application.html;

import java.util.function.Function;

public class SimpleListHtmlColumnModel<E, X, Y> {

  // required properties
  private final SimpleListHtmlTableModel<E> tableModel;
  private final String title;
  private final Function<X, Y> propertyGetter;

  // optional properties
  private Function<Y, ?> converter = Function.identity();
  private boolean headerColumn = false;
  private String columnClass;
  private String titleClass;
  private String dataClass;

  SimpleListHtmlColumnModel(
      SimpleListHtmlTableModel<E> tableModel, String title, Function<X, Y> propertyGetter) {
    this.tableModel = tableModel;
    this.title = title;
    this.propertyGetter = propertyGetter;
  }

  public SimpleListHtmlTableModel<E> endColumn() {
    return tableModel;
  }

  public SimpleListHtmlColumnModel<E, X, Y> converter(Function<Y, ?> converter) {
    this.converter = converter;
    return this;
  }

  public SimpleListHtmlColumnModel<E, X, Y> headerColumn(boolean headerColumn) {
    this.headerColumn = headerColumn;
    return this;
  }

  public SimpleListHtmlColumnModel<E, X, Y> columnClass(String columnClass) {
    this.columnClass = columnClass;
    return this;
  }

  public SimpleListHtmlColumnModel<E, X, Y> titleClass(String titleClass) {
    this.titleClass = titleClass;
    return this;
  }

  public SimpleListHtmlColumnModel<E, X, Y> dataClass(String dataClass) {
    this.dataClass = dataClass;
    return this;
  }

  public boolean isHeaderColumn() {
    return headerColumn;
  }

  public String getTitle() {
    return title;
  }

  public String getColumnClass() {
    return columnClass;
  }

  public String getTitleClass() {
    return titleClass;
  }

  public String getDataClass() {
    return dataClass;
  }

  public Function<X, ?> getPropertyFunction() {
    return propertyGetter.andThen(converter);
  }
}
