package com.gmail.kasabuta4.jsfdemo.common.view.html;

import java.util.Map;
import java.util.function.Function;

public abstract class HtmlAbstractTable<T extends HtmlAbstractTable, X> {

  // optional properties
  private String tableClass;
  private String caption;
  private Function<X, Object> highlightFunction;
  private Map<Object, String> highlightClasses;
  private HtmlSequenceColumn<T, X> sequenceColumn;

  protected abstract T self();

  public T tableClass(String tableClass) {
    this.tableClass = tableClass;
    return self();
  }

  public T caption(String caption) {
    this.caption = caption;
    return self();
  }

  public T highlight(Function<X, Object> highlightFunction, Map<Object, String> highlightClasses) {
    this.highlightFunction = highlightFunction;
    this.highlightClasses = highlightClasses;
    return self();
  }

  public HtmlSequenceColumn<T, X> addSequenceColumn(String header) {
    return sequenceColumn = new HtmlSequenceColumn<>(self(), header);
  }

  public String getTableClass() {
    return tableClass;
  }

  public String getCaption() {
    return caption;
  }

  public HtmlSequenceColumn<T, X> getSequenceColumn() {
    return sequenceColumn;
  }

  public int getHeaderRowCount() {
    return 0;
  }

  public String bodyRecordClass(X x) {
    return highlightClass(x);
  }

  private String highlightClass(X x) {
    return (highlightFunction == null || highlightClasses == null)
        ? null
        : highlightClasses.get(highlightFunction.apply(x));
  }
}
