package com.gmail.kasabuta4.jsfdemo.common.view.html;

import java.util.function.Function;

public abstract class HtmlAbstractTable<T extends HtmlAbstractTable, X> {

  // optional properties
  private HtmlSequenceColumn<T, X> sequenceColumn;
  private String caption;
  private String tableClass;
  private Function<X, Object> highlightPropertyGetter;
  private Function<Object, String> highlightClassConverter;

  protected abstract T self();

  public T caption(String caption) {
    this.caption = caption;
    return self();
  }

  public T tableClass(String tableClass) {
    this.tableClass = tableClass;
    return self();
  }

  public T highlight(
      Function<X, Object> highlightPropertyGetter,
      Function<Object, String> highlightClassConverter) {
    this.highlightPropertyGetter = highlightPropertyGetter;
    this.highlightClassConverter = highlightClassConverter;
    return self();
  }

  public T highlightPropertyGetter(Function<X, Object> highlightPropertyGetter) {
    this.highlightPropertyGetter = highlightPropertyGetter;
    return self();
  }

  public HtmlSequenceColumn<T, X> addSequenceColumn(String header) {
    return sequenceColumn = new HtmlSequenceColumn<>(self(), header);
  }

  public HtmlSequenceColumn<T, X> getSequenceColumn() {
    return sequenceColumn;
  }

  public String getCaption() {
    return caption;
  }

  public String getTableClass() {
    return tableClass;
  }

  public int getHeaderRowCount() {
    return 0;
  }

  public String bodyRecordClass(X x) {
    return highlightClass(x);
  }

  Function<X, Object> getHighlightPropertyGetter() {
    return highlightPropertyGetter;
  }

  private String highlightClass(X x) {
    return (highlightPropertyGetter == null || highlightClassConverter == null)
        ? null
        : highlightPropertyGetter.andThen(highlightClassConverter).apply(x);
  }
}
