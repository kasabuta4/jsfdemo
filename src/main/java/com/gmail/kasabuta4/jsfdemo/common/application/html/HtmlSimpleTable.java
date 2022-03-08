package com.gmail.kasabuta4.jsfdemo.common.application.html;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class HtmlSimpleTable<E> {

  // required properties
  private final List<E> data;
  private List<HtmlSimpleColumn<E, E, ?>> columns = new ArrayList<>();

  // optional properties
  private String caption;
  private String tableClass;
  private HtmlSimpleColumn<E, Integer, Integer> sequenceColumn;

  public HtmlSimpleTable(List<E> data) {
    this.data = data;
  }

  public HtmlSimpleTable<E> caption(String caption) {
    this.caption = caption;
    return this;
  }

  public HtmlSimpleTable<E> tableClass(String tableClass) {
    this.tableClass = tableClass;
    return this;
  }

  public HtmlSimpleColumn<E, Integer, Integer> addSequenceColumn(String header) {
    return sequenceColumn = new HtmlSimpleColumn<>(this, header, Function.identity());
  }

  public <V> HtmlSimpleColumn<E, E, V> addSimpleColumn(
      String header, Function<E, V> propertyGetter) {
    HtmlSimpleColumn<E, E, V> column = new HtmlSimpleColumn<>(this, header, propertyGetter);
    columns.add(column);
    return column;
  }

  public String getCaption() {
    return caption;
  }

  public List<E> getData() {
    return data;
  }

  public List<HtmlSimpleColumn<E, E, ?>> getColumns() {
    return columns;
  }

  public String getTableClass() {
    return tableClass;
  }

  public HtmlSimpleColumn<E, Integer, Integer> getSequenceColumn() {
    return sequenceColumn;
  }
}
