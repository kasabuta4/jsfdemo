package com.gmail.kasabuta4.jsfdemo.common.view.html;

import java.util.function.Function;

public class HtmlYTitle<X, Y, E, V, P> {

  // required properties
  private final HtmlYColumn<X, Y, E, V> yColumn;
  private final Function<Y, P> propertyGetter;

  // optional properties
  private Function<P, ?> converter = Function.identity();
  private String headerCellClass;

  HtmlYTitle(HtmlYColumn<X, Y, E, V> yColumn, Function<Y, P> propertyGetter) {
    this.yColumn = yColumn;
    this.propertyGetter = propertyGetter;
  }

  public HtmlYColumn<X, Y, E, V> endYTitle() {
    return yColumn;
  }

  public HtmlYTitle<X, Y, E, V, P> converter(Function<P, ?> converter) {
    this.converter = converter;
    return this;
  }

  public HtmlYTitle<X, Y, E, V, P> headerCellClass(String headerCellClass) {
    this.headerCellClass = headerCellClass;
    return this;
  }

  public Function<Y, ?> getProperty() {
    return propertyGetter.andThen(converter);
  }

  public String getHeaderCellClass() {
    return headerCellClass;
  }
}
