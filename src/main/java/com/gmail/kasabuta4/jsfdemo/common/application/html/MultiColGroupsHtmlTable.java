package com.gmail.kasabuta4.jsfdemo.common.application.html;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class MultiColGroupsHtmlTable<R, C, E> {

  // required properties
  private final String title;
  private final Map<R, Map<C, E>> data;
  private final List<R> rowKeys;
  private final List<C> columnKeys;
  private List<MultiColGroupsHtmlTableRowKeyColumn<R, C, E, ?>> rowKeyColumns = new ArrayList<>();
  private final List<MultiColGroupsHtmlTableColumnGroup<R, C, E, ?>> columnGroups =
      new ArrayList<>();

  public MultiColGroupsHtmlTable(String title, Map<R, Map<C, E>> data) {
    this.title = title;
    this.data = data;
    this.rowKeys = data.keySet().stream().sorted().collect(toList());
    this.columnKeys =
        data.values().stream()
            .flatMap(map -> map.keySet().stream())
            .distinct()
            .sorted()
            .collect(toList());
  }

  public <V> MultiColGroupsHtmlTableRowKeyColumn<R, C, E, V> addRowKeyColumn(
      String title, Function<R, V> propertyGetter) {
    MultiColGroupsHtmlTableRowKeyColumn<R, C, E, V> rowKeyColumn =
        new MultiColGroupsHtmlTableRowKeyColumn<>(this, title, propertyGetter);
    rowKeyColumns.add(rowKeyColumn);
    return rowKeyColumn;
  }

  public MultiColGroupsHtmlTableRowKeyColumn<R, C, E, R> addDefaultRowKeyColumn(String title) {
    MultiColGroupsHtmlTableRowKeyColumn<R, C, E, R> rowKeyColumn =
        new MultiColGroupsHtmlTableRowKeyColumn<>(this, title, Function.identity());
    rowKeyColumns.add(rowKeyColumn);
    return rowKeyColumn;
  }

  public <V> MultiColGroupsHtmlTableColumnGroup<R, C, E, V> addColumnGroup(
      String title, Predicate<C> filter, Function<E, V> propertyGetter) {
    MultiColGroupsHtmlTableColumnGroup<R, C, E, V> columnGroup =
        new MultiColGroupsHtmlTableColumnGroup<>(this, columnKeys, title, filter, propertyGetter);
    columnGroups.add(columnGroup);
    return columnGroup;
  }

  public int getColumnTitleRowCount() {
    return columnGroups.stream()
        .map(MultiColGroupsHtmlTableColumnGroup::getColumnTitles)
        .map(List::size)
        .max(Integer::compare)
        .orElse(0);
  }

  public List<Integer> getColumnTitleRowIndices() {
    return Stream.iterate(0, i -> i + 1).limit(getColumnTitleRowCount()).collect(toList());
  }

  public String getTitle() {
    return title;
  }

  public Map<R, Map<C, E>> getData() {
    return data;
  }

  public List<R> getRowKeys() {
    return rowKeys;
  }

  public List<MultiColGroupsHtmlTableRowKeyColumn<R, C, E, ?>> getRowKeyColumns() {
    return rowKeyColumns;
  }

  public List<MultiColGroupsHtmlTableColumnGroup<R, C, E, ?>> getColumnGroups() {
    return columnGroups;
  }
}
