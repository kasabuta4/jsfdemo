package com.gmail.kasabuta4.jsfdemo.common.view.html;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class HtmlXYTable<X, Y, E> {

  // required properties
  private final Map<X, Map<Y, E>> data;
  private final List<X> xList;
  private final List<Y> yList;
  private List<HtmlXColumn<X, Y, E, ?>> xColumns = new ArrayList<>();
  private final List<HtmlYColumn<X, Y, E, ?>> yColumns = new ArrayList<>();

  // optional properties
  private String caption;
  private String tableClass;

  public HtmlXYTable(Map<X, Map<Y, E>> data) {
    this.data = data;
    this.xList = data.keySet().stream().sorted().collect(toList());
    this.yList =
        data.values().stream()
            .flatMap(map -> map.keySet().stream())
            .distinct()
            .sorted()
            .collect(toList());
  }

  public HtmlXYTable<X, Y, E> caption(String caption) {
    this.caption = caption;
    return this;
  }

  public HtmlXYTable<X, Y, E> tableClass(String tableClass) {
    this.tableClass = tableClass;
    return this;
  }

  public <V> HtmlXColumn<X, Y, E, V> addXColumn(String header, Function<X, V> propertyGetter) {
    HtmlXColumn<X, Y, E, V> xColumn = new HtmlXColumn<>(this, header, propertyGetter);
    xColumns.add(xColumn);
    return xColumn;
  }

  public HtmlXColumn<X, Y, E, X> addIdentityXColumn(String header) {
    HtmlXColumn<X, Y, E, X> xColumn = new HtmlXColumn<>(this, header, Function.identity());
    xColumns.add(xColumn);
    return xColumn;
  }

  public <V> HtmlYColumn<X, Y, E, V> addYColumn(
      String header, Predicate<Y> yFilter, Function<E, V> propertyGetter) {
    HtmlYColumn<X, Y, E, V> yColumn =
        new HtmlYColumn<>(this, yList, header, yFilter, propertyGetter);
    yColumns.add(yColumn);
    return yColumn;
  }

  public int getMaxYTitles() {
    return yColumns.stream().map(HtmlYColumn::getyTitles).mapToInt(List::size).max().orElse(0);
  }

  public List<Integer> getyTitleIndices() {
    return Stream.iterate(0, i -> i + 1).limit(getMaxYTitles()).collect(toList());
  }

  public Map<X, Map<Y, E>> getData() {
    return data;
  }

  public List<X> getxList() {
    return xList;
  }

  public List<HtmlXColumn<X, Y, E, ?>> getxColumns() {
    return xColumns;
  }

  public List<HtmlYColumn<X, Y, E, ?>> getyColumns() {
    return yColumns;
  }

  public String getCaption() {
    return caption;
  }

  public String getTableClass() {
    return tableClass;
  }
}
