package com.gmail.kasabuta4.jsfdemo.common.application.html;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class MultiColGroupsHtmlTableColumnGroup<R, C, E, V> {

  // required properties
  private final MultiColGroupsHtmlTable<R, C, E> table;
  private final String title;
  private final List<C> columnKeys;
  private final Function<E, V> propertyGetter;

  // optional properties
  private Function<V, ?> converter = Function.identity();
  private List<Function<C, ?>> columnTitles = defaultColumnTitles();
  private String colgroupClass;

  MultiColGroupsHtmlTableColumnGroup(
      MultiColGroupsHtmlTable<R, C, E> table,
      List<C> columnKeys,
      String title,
      Predicate<C> filter,
      Function<E, V> propertyGetter) {
    this.table = table;
    this.title = title;
    this.columnKeys = columnKeys.stream().filter(filter).collect(toList());
    this.propertyGetter = propertyGetter;
  }

  public MultiColGroupsHtmlTable<R, C, E> endColumnGroup() {
    return table;
  }

  public MultiColGroupsHtmlTableColumnGroup<R, C, E, V> converter(Function<V, ?> converter) {
    this.converter = converter;
    return this;
  }

  public MultiColGroupsHtmlTableColumnGroup<R, C, E, V> columnTitles(
      List<Function<C, ?>> columnTitles) {
    this.columnTitles = columnTitles;
    return this;
  }

  public MultiColGroupsHtmlTableColumnGroup<R, C, E, V> colgroupClass(String colgroupClass) {
    this.colgroupClass = colgroupClass;
    return this;
  }

  public int columnTitleRowspan(int index) {
    return index < columnTitles.size() - 1 ? 1 : table.getColumnTitleRowCount() - index;
  }

  public String getTitle() {
    return title;
  }

  public List<C> getColumnKeys() {
    return columnKeys;
  }

  public Function<E, ?> getProperty() {
    return propertyGetter.andThen(converter);
  }

  public List<Function<C, ?>> getColumnTitles() {
    return columnTitles;
  }

  public String getColgroupClass() {
    return colgroupClass;
  }

  private List<Function<C, ?>> defaultColumnTitles() {
    List<Function<C, ?>> result = new ArrayList<>(1);
    result.add(Function.identity());
    return unmodifiableList(result);
  }
}
