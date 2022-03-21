package com.gmail.kasabuta4.jsfdemo.common.view.html;

import java.util.function.Function;

public abstract class HtmlAbstractColumn<
    C extends HtmlAbstractColumn, T extends HtmlAbstractTable, E, V, X> {

  // required properties
  protected final T table;
  private final String header;
  private final Function<E, V> propertyGetter;

  // optional properties
  private Function<V, ?> converter = Function.identity();
  private boolean headerColumn = false;
  private String columnClass;
  private String headerCellClass;
  private String bodyCellClass;
  private Function<X, Object> highlightPropertyGetter;
  private Function<Object, String> highlightClassConverter;

  protected HtmlAbstractColumn(T table, String header, Function<E, V> propertyGetter) {
    this.table = table;
    this.header = header;
    this.propertyGetter = propertyGetter;
  }

  protected abstract C self();

  public T endColumn() {
    return table;
  }

  public C converter(Function<V, ?> converter) {
    this.converter = converter;
    return self();
  }

  public C headerColumn(boolean headerColumn) {
    this.headerColumn = headerColumn;
    return self();
  }

  public C columnClass(String columnClass) {
    this.columnClass = columnClass;
    return self();
  }

  public C headerCellClass(String headerCellClass) {
    this.headerCellClass = headerCellClass;
    return self();
  }

  public C bodyCellClass(String bodyCellClass) {
    this.bodyCellClass = bodyCellClass;
    return self();
  }

  public C highlight(
      Function<X, Object> highlightPropertyGetter,
      Function<Object, String> highlightClassConverter) {
    this.highlightPropertyGetter = highlightPropertyGetter;
    this.highlightClassConverter = highlightClassConverter;
    return self();
  }

  public C highlightClassConverter(Function<Object, String> highlightClassConverter) {
    this.highlightClassConverter = highlightClassConverter;
    return self();
  }

  public String getHeader() {
    return header;
  }

  public Function<E, ?> getProperty() {
    return propertyGetter.andThen(converter);
  }

  public boolean isHeaderColumn() {
    return headerColumn;
  }

  public String getColumnClass() {
    return columnClass;
  }

  public String getHeaderCellClass() {
    return headerCellClass;
  }

  public String bodyCellClass(X x) {
    String highlightClass = highlightClass(x);
    if (bodyCellClass == null && highlightClass == null) return null;
    else if (highlightClass == null) return bodyCellClass;
    else if (bodyCellClass == null) return highlightClass;
    else return String.join(" ", bodyCellClass, highlightClass);
  }

  private String highlightClass(X x) {
    return (getHighlightPropertyGetter() == null || highlightClassConverter == null)
        ? null
        : getHighlightPropertyGetter().andThen(highlightClassConverter).apply(x);
  }

  private Function<X, Object> getHighlightPropertyGetter() {
    return highlightPropertyGetter == null
        ? table.getHighlightPropertyGetter()
        : highlightPropertyGetter;
  }
}
