package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import java.util.function.Function;

public class SimpleColumn<T extends AbstractTable, E, V>
    extends AbstractColumn<SimpleColumn<T, E, V>, T, E, V> {

  protected SimpleColumn(
      T table,
      String header,
      Function<E, V> property,
      ColumnWidthConfigurator columnWidthConfigurator,
      NumberFormat format) {
    super(table, header, property, columnWidthConfigurator, format);
  }

  @Override
  protected SimpleColumn<T, E, V> self() {
    return this;
  }
}
