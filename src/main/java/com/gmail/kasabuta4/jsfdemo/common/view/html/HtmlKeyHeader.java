package com.gmail.kasabuta4.jsfdemo.common.view.html;

import java.util.function.Function;

public class HtmlKeyHeader<HtmlMapColumn, Y, P> {

  // required properties
  private final HtmlMapColumn mapColumn;
  private final Function<Y, P> propertyGetter;

  // optional properties
  private Function<P, ?> converter = Function.identity();
  private String headerCellClass;

  HtmlKeyHeader(HtmlMapColumn mapColumn, Function<Y, P> propertyGetter) {
    this.mapColumn = mapColumn;
    this.propertyGetter = propertyGetter;
  }

  public HtmlMapColumn endKeyHeader() {
    return mapColumn;
  }

  public HtmlKeyHeader<HtmlMapColumn, Y, P> converter(Function<P, ?> converter) {
    this.converter = converter;
    return this;
  }

  public HtmlKeyHeader<HtmlMapColumn, Y, P> headerCellClass(String headerCellClass) {
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
