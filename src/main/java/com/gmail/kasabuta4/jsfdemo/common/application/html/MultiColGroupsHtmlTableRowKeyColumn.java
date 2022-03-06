package com.gmail.kasabuta4.jsfdemo.common.application.html;

import java.util.function.Function;

public class MultiColGroupsHtmlTableRowKeyColumn<R, C, E, V> {

  // required properties
  private final MultiColGroupsHtmlTable<R, C, E> table;
  private final String title;
  private final Function<R, V> propertyGetter;

  // optional properties
  private String columnClass;
  private Function<V, ?> converter = Function.identity();

  MultiColGroupsHtmlTableRowKeyColumn(
      MultiColGroupsHtmlTable<R, C, E> table, String title, Function<R, V> propertyGetter) {
    this.table = table;
    this.title = title;
    this.propertyGetter = propertyGetter;
  }

  public MultiColGroupsHtmlTable<R, C, E> endRowKeyColumn() {
    return table;
  }

  public MultiColGroupsHtmlTableRowKeyColumn<R, C, E, V> columnClass(String columnClass) {
    this.columnClass = columnClass;
    return this;
  }

  public MultiColGroupsHtmlTableRowKeyColumn<R, C, E, V> converter(Function<V, ?> converter) {
    this.converter = converter;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public Function<R, ?> getProperty() {
    return propertyGetter.andThen(converter);
  }

  public String getColumnClass() {
    return columnClass;
  }
}
