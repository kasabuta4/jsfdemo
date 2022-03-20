package com.gmail.kasabuta4.jsfdemo.common.view.html;

import java.util.function.Function;

public class HtmlSequenceColumn<T extends HtmlAbstractTable, E>
    extends HtmlAbstractColumn<HtmlSequenceColumn<T, E>, T, Integer, Integer, E> {

  protected HtmlSequenceColumn(T table, String header) {
    super(table, header, Function.identity());
  }

  @Override
  protected HtmlSequenceColumn<T, E> self() {
    return this;
  }
}
