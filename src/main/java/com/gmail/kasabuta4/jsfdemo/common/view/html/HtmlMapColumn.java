package com.gmail.kasabuta4.jsfdemo.common.view.html;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class HtmlMapColumn<T extends HtmlAbstractTable, Y, E, V>
    extends HtmlAbstractColumn<HtmlMapColumn<T, Y, E, V>, T, E, V> {

  // required properties
  private final List<Y> keys;
  private final List<HtmlKeyHeader<HtmlMapColumn<T, Y, E, V>, Y, ?>> keyHeaders = new ArrayList<>();

  protected HtmlMapColumn(
      T table, String header, Function<E, V> propertyGetter, List<Y> keys, Predicate<Y> keyFilter) {
    super(table, header, propertyGetter);
    this.keys = keys.stream().filter(keyFilter).collect(toList());
  }

  @Override
  protected HtmlMapColumn<T, Y, E, V> self() {
    return this;
  }

  public HtmlKeyHeader<HtmlMapColumn<T, Y, E, V>, Y, Y> addIdentityKeyHeader() {
    HtmlKeyHeader<HtmlMapColumn<T, Y, E, V>, Y, Y> keyHeader =
        new HtmlKeyHeader<>(this, Function.identity());
    keyHeaders.add(keyHeader);
    return keyHeader;
  }

  public int rowspanOfKeyHeader(int index) {
    return index < keyHeaders.size() - 1 ? 1 : table.getHeaderRowCount() - index;
  }

  public List<Y> getKeys() {
    return keys;
  }

  public List<HtmlKeyHeader<HtmlMapColumn<T, Y, E, V>, Y, ?>> getKeyHeaders() {
    return keyHeaders;
  }
}
