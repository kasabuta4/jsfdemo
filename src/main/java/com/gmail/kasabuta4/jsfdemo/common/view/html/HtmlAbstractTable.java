package com.gmail.kasabuta4.jsfdemo.common.view.html;

import java.util.function.Function;

public abstract class HtmlAbstractTable<T extends HtmlAbstractTable> {

  // optional properties
  private String tableClass;
  private String caption;
  private HtmlSimpleColumn<T, Integer, Integer> sequenceColumn;

  protected abstract T self();

  public T tableClass(String tableClass) {
    this.tableClass = tableClass;
    return self();
  }

  public T caption(String caption) {
    this.caption = caption;
    return self();
  }

  public HtmlSimpleColumn<T, Integer, Integer> addSequenceColumn(String header) {
    return sequenceColumn = new HtmlSimpleColumn<>(self(), header, Function.identity());
  }

  public String getTableClass() {
    return tableClass;
  }

  public String getCaption() {
    return caption;
  }

  public HtmlSimpleColumn<T, Integer, Integer> getSequenceColumn() {
    return sequenceColumn;
  }

  public int getHeaderRowCount() {
    return 0;
  }
}
