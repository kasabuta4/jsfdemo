package com.gmail.kasabuta4.jsfdemo.common.view.html;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class HtmlYColumn<X, Y, E, V> {

  // required properties
  private final HtmlXYTable<X, Y, E> table;
  private final String header;
  private final List<Y> yList;
  private final List<HtmlYTitle<X, Y, E, V, ?>> yTitles = new ArrayList<>();
  private final Function<E, V> propertyGetter;

  // optional properties
  private Function<V, ?> converter = Function.identity();
  private String columnClass;
  private String headerCellClass;
  private String bodyCellClass;

  HtmlYColumn(
      HtmlXYTable<X, Y, E> table,
      List<Y> yList,
      String title,
      Predicate<Y> yFilter,
      Function<E, V> propertyGetter) {
    this.table = table;
    this.header = title;
    this.yList = yList.stream().filter(yFilter).collect(toList());
    this.propertyGetter = propertyGetter;
  }

  public HtmlXYTable<X, Y, E> endYColumn() {
    return table;
  }

  public HtmlYColumn<X, Y, E, V> converter(Function<V, ?> converter) {
    this.converter = converter;
    return this;
  }

  public HtmlYColumn<X, Y, E, V> columnClass(String columnClass) {
    this.columnClass = columnClass;
    return this;
  }

  public HtmlYColumn<X, Y, E, V> headerCellClass(String headerCellClass) {
    this.headerCellClass = headerCellClass;
    return this;
  }

  public HtmlYColumn<X, Y, E, V> bodyCellClass(String bodyCellClass) {
    this.bodyCellClass = bodyCellClass;
    return this;
  }

  public HtmlYTitle<X, Y, E, V, Y> addIdentityYTitle() {
    HtmlYTitle<X, Y, E, V, Y> yTitle = new HtmlYTitle<>(this, Function.identity());
    yTitles.add(yTitle);
    return yTitle;
  }

  public int headerCellRowspan(int index) {
    return index < yTitles.size() - 1 ? 1 : table.getMaxYTitles() - index;
  }

  public String getHeader() {
    return header;
  }

  public List<Y> getyList() {
    return yList;
  }

  public List<HtmlYTitle<X, Y, E, V, ?>> getyTitles() {
    return yTitles;
  }

  public Function<E, ?> getProperty() {
    return propertyGetter.andThen(converter);
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
