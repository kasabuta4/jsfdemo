package com.gmail.kasabuta4.jsfdemo.common.view.html;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class HtmlSimpleTable<E> extends HtmlAbstractTable<HtmlSimpleTable<E>> {

  // required properties
  private final List<E> data;
  private List<HtmlSimpleColumn<HtmlSimpleTable<E>, E, ?>> columns = new ArrayList<>();

  public HtmlSimpleTable(List<E> data) {
    this.data = data;
  }

  @Override
  protected HtmlSimpleTable<E> self() {
    return this;
  }

  public <V> HtmlSimpleColumn<HtmlSimpleTable<E>, E, V> addSimpleColumn(
      String header, Function<E, V> propertyGetter) {
    HtmlSimpleColumn<HtmlSimpleTable<E>, E, V> column =
        new HtmlSimpleColumn<>(this, header, propertyGetter);
    columns.add(column);
    return column;
  }

  public List<E> getData() {
    return data;
  }

  public List<HtmlSimpleColumn<HtmlSimpleTable<E>, E, ?>> getColumns() {
    return columns;
  }
}
