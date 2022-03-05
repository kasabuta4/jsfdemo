package com.gmail.kasabuta4.jsfdemo.common.application.html;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SimpleListHtmlTableModel<E> {

  // required properties
  private final List<E> data;
  private List<SimpleListHtmlColumnModel<E, E, ?>> columns = new ArrayList<>();

  // optional properties
  private String title;
  private String tableClass;
  private SimpleListHtmlColumnModel<E, Integer, Integer> sequenceColumn;

  public SimpleListHtmlTableModel(List<E> data) {
    this.data = data;
  }

  public SimpleListHtmlTableModel<E> title(String title) {
    this.title = title;
    return this;
  }

  public SimpleListHtmlTableModel<E> tableClass(String tableClass) {
    this.tableClass = tableClass;
    return this;
  }

  public SimpleListHtmlColumnModel<E, Integer, Integer> addSequenceColumn(String title) {
    return sequenceColumn = new SimpleListHtmlColumnModel<>(this, title, Function.identity());
  }

  public <V> SimpleListHtmlColumnModel<E, E, V> addColumn(
      String title, Function<E, V> propertyGetter) {
    SimpleListHtmlColumnModel<E, E, V> column =
        new SimpleListHtmlColumnModel<>(this, title, propertyGetter);
    columns.add(column);
    return column;
  }

  public String getTitle() {
    return title;
  }

  public List<E> getData() {
    return data;
  }

  public List<SimpleListHtmlColumnModel<E, E, ?>> getColumns() {
    return columns;
  }

  public String getTableClass() {
    return tableClass;
  }

  public SimpleListHtmlColumnModel<E, Integer, Integer> getSequenceColumn() {
    return sequenceColumn;
  }
}
