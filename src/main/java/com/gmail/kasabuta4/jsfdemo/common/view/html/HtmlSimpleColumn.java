package com.gmail.kasabuta4.jsfdemo.common.view.html;

import java.util.function.Function;

public class HtmlSimpleColumn<T extends HtmlAbstractTable, E, V>
    extends HtmlAbstractColumn<HtmlSimpleColumn<T, E, V>, T, E, V> {

  // optional properties
  private boolean headerColumn = false;

  protected HtmlSimpleColumn(T table, String header, Function<E, V> propertyGetter) {
    super(table, header, propertyGetter);
  }

  @Override
  protected HtmlSimpleColumn<T, E, V> self() {
    return this;
  }

  public HtmlSimpleColumn<T, E, V> headerColumn(boolean headerColumn) {
    this.headerColumn = headerColumn;
    return this;
  }

  public boolean isHeaderColumn() {
    return headerColumn;
  }
}
