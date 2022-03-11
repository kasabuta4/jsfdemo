package com.gmail.kasabuta4.jsfdemo.common.view.html;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class HtmlMapTable<X, Y, E> extends HtmlAbstractTable<HtmlMapTable<X, Y, E>> {

  // required properties
  private final Map<X, Map<Y, E>> data;
  private final List<X> rowKeys;
  private final List<Y> columnKeys;
  private List<HtmlSimpleColumn<HtmlMapTable<X, Y, E>, X, ?>> keyColumns = new ArrayList<>();
  private final List<HtmlMapColumn<HtmlMapTable<X, Y, E>, Y, E, ?>> valueColumns =
      new ArrayList<>();

  public HtmlMapTable(Map<X, Map<Y, E>> data) {
    this.data = data;
    this.rowKeys = data.keySet().stream().sorted().collect(toList());
    this.columnKeys =
        data.values().stream()
            .flatMap(map -> map.keySet().stream())
            .distinct()
            .sorted()
            .collect(toList());
  }

  @Override
  protected HtmlMapTable<X, Y, E> self() {
    return this;
  }

  public <V> HtmlSimpleColumn<HtmlMapTable<X, Y, E>, X, V> addXColumn(
      String header, Function<X, V> propertyGetter) {
    HtmlSimpleColumn<HtmlMapTable<X, Y, E>, X, V> xColumn =
        new HtmlSimpleColumn<>(this, header, propertyGetter);
    keyColumns.add(xColumn);
    return xColumn;
  }

  public HtmlSimpleColumn<HtmlMapTable<X, Y, E>, X, X> addIdentityXColumn(String header) {
    HtmlSimpleColumn<HtmlMapTable<X, Y, E>, X, X> xColumn =
        new HtmlSimpleColumn<>(this, header, Function.identity());
    keyColumns.add(xColumn);
    return xColumn;
  }

  public <V> HtmlMapColumn<HtmlMapTable<X, Y, E>, Y, E, V> addYColumn(
      String header, Function<E, V> propertyGetter, Predicate<Y> yFilter) {
    HtmlMapColumn<HtmlMapTable<X, Y, E>, Y, E, V> yColumn =
        new HtmlMapColumn<>(this, header, propertyGetter, columnKeys, yFilter);
    valueColumns.add(yColumn);
    return yColumn;
  }

  @Override
  public int getHeaderRowCount() {
    return valueColumns.stream()
            .map(HtmlMapColumn::getKeyHeaders)
            .mapToInt(List::size)
            .max()
            .orElse(0)
        + 1;
  }

  public List<Integer> getKeyHeaderIndices() {
    return Stream.iterate(0, i -> i + 1).limit(getHeaderRowCount() - 1).collect(toList());
  }

  public Map<X, Map<Y, E>> getData() {
    return data;
  }

  public List<X> getRowKeys() {
    return rowKeys;
  }

  public List<HtmlSimpleColumn<HtmlMapTable<X, Y, E>, X, ?>> getKeyColumns() {
    return keyColumns;
  }

  public List<HtmlMapColumn<HtmlMapTable<X, Y, E>, Y, E, ?>> getValueColumns() {
    return valueColumns;
  }
}
