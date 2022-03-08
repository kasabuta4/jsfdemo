package com.gmail.kasabuta4.jsfdemo.common.application.html;

import static java.util.Collections.unmodifiableList;
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
  private final Function<E, V> propertyGetter;

  // optional properties
  private Function<V, ?> converter = Function.identity();
  private List<Function<Y, ?>> yTitles = defaultColumnTitles();
  private String columnClass;

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

  public HtmlYColumn<X, Y, E, V> yTitles(List<Function<Y, ?>> yTitles) {
    this.yTitles = yTitles;
    return this;
  }

  public HtmlYColumn<X, Y, E, V> columnClass(String columnClass) {
    this.columnClass = columnClass;
    return this;
  }

  public int columnTitleRowspan(int index) {
    return index < yTitles.size() - 1 ? 1 : table.getColumnTitleRowCount() - index;
  }

  public String getHeader() {
    return header;
  }

  public List<Y> getyList() {
    return yList;
  }

  public Function<E, ?> getProperty() {
    return propertyGetter.andThen(converter);
  }

  public List<Function<Y, ?>> getyTitles() {
    return yTitles;
  }

  public String getColumnClass() {
    return columnClass;
  }

  private static <Y> List<Function<Y, ?>> defaultColumnTitles() {
    List<Function<Y, ?>> result = new ArrayList<>(1);
    result.add(Function.identity());
    return unmodifiableList(result);
  }
}
