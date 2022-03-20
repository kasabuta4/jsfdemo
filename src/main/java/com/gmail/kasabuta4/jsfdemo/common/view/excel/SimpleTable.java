package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SimpleTable<E> extends AbstractTable<SimpleTable<E>, E> {

  // required properties
  private final List<E> data;
  private final List<SimpleColumn<SimpleTable<E>, E, ?>> columns = new ArrayList<>();

  protected SimpleTable(WorkbookModel workbookModel, String sheetName, List<E> data) {
    super(workbookModel, sheetName);
    this.data = data;
  }

  @Override
  protected SimpleTable<E> self() {
    return this;
  }

  public <V> SimpleColumn<SimpleTable<E>, E, V> addColumn(
      String header, Function<E, V> property, ColumnWidth columnWidthConfigurator) {
    SimpleColumn<SimpleTable<E>, E, V> columnModel =
        new SimpleColumn<>(this, header, property, columnWidthConfigurator);
    columns.add(columnModel);
    return columnModel;
  }

  @Override
  protected void initStyles() {
    super.initStyles();
    columns.stream().forEach(SimpleColumn::initStyles);
  }

  @Override
  protected void writeHeader() {
    super.writeHeader();
    writeColumnsHeader();
  }

  private void writeColumnsHeader() {
    int headerRowCount = getHeaderRowCount();
    for (int i = 0; i < columns.size(); i++)
      columns.get(i).writeHeader(worksheet, captionRowCount, columnIndex(i), headerRowCount);
  }

  @Override
  protected void writeBody() {
    for (int dataIndex = 0; dataIndex < data.size(); dataIndex++) writeRecord(dataIndex);
  }

  @Override
  protected void writeRecord(int dataIndex) {
    super.writeRecord(dataIndex);
    writeSequenceColumnToRecord(dataIndex);
    writeColumnsToRecord(dataIndex);
  }

  private void writeSequenceColumnToRecord(int dataIndex) {
    sequenceColumn.writeRecord(data.get(dataIndex), dataIndex, worksheet, toRowIndex(dataIndex), 0);
  }

  private void writeColumnsToRecord(int dataIndex) {
    E entity = data.get(dataIndex);
    int rowIndex = toRowIndex(dataIndex);
    for (int i = 0; i < columns.size(); i++)
      columns.get(i).writeRecord(entity, dataIndex, worksheet, rowIndex, columnIndex(i));
  }

  @Override
  protected void configureColumnWidth() {
    super.configureColumnWidth();
    configureColumnsWidth();
  }

  private void configureColumnsWidth() {
    for (int i = 0; i < columns.size(); i++)
      columns.get(i).configureColumnWidth(worksheet, columnIndex(i));
  }

  @Override
  protected int calculateColumnsCount() {
    return super.calculateColumnsCount() + columns.size();
  }
}
