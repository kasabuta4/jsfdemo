package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class MapTable<X, Y, E> extends AbstractTable<MapTable<X, Y, E>, X> {

  // required properties
  private final Map<X, Map<Y, E>> data;
  private final List<X> rowKeys;
  private final List<Y> columnKeys;
  private List<SimpleColumn<MapTable<X, Y, E>, X, ?>> keyColumns = new ArrayList<>();
  private List<MapColumn<MapTable<X, Y, E>, X, Y, E, ?>> valueColumns = new ArrayList<>();

  protected MapTable(WorkbookModel workbookModel, String sheetName, Map<X, Map<Y, E>> data) {
    super(workbookModel, sheetName);
    this.data = data;
    this.rowKeys = data.keySet().stream().sorted().collect(toList());
    this.columnKeys =
        data.values().stream()
            .flatMap(map -> map.keySet().stream())
            .distinct()
            .sorted()
            .collect(toList());
  }

  @Override
  protected MapTable<X, Y, E> self() {
    return this;
  }

  public SimpleColumn<MapTable<X, Y, E>, X, X> addIdentityKeyColumn(
      String header, ColumnWidth columnWidthConfigurator) {
    return addKeyColumn(header, Function.identity(), columnWidthConfigurator);
  }

  public <V> SimpleColumn<MapTable<X, Y, E>, X, V> addKeyColumn(
      String header, Function<X, V> propertyGetter, ColumnWidth columnWidthConfigurator) {
    SimpleColumn<MapTable<X, Y, E>, X, V> keyColumn =
        new SimpleColumn<>(this, header, propertyGetter, columnWidthConfigurator);
    keyColumns.add(keyColumn);
    return keyColumn;
  }

  public MapColumn<MapTable<X, Y, E>, X, Y, E, E> addIdentityValueColumn(
      String header, ColumnWidth columnWidthConfigurator, Predicate<Y> columnKeyFilter) {
    return addValueColumn(header, Function.identity(), columnWidthConfigurator, columnKeyFilter);
  }

  public <V> MapColumn<MapTable<X, Y, E>, X, Y, E, V> addValueColumn(
      String header,
      Function<E, V> propertyGetter,
      ColumnWidth columnWidthConfigurator,
      Predicate<Y> columnKeyFilter) {
    MapColumn<MapTable<X, Y, E>, X, Y, E, V> valueColumn =
        new MapColumn<>(
            this, header, propertyGetter, columnWidthConfigurator, columnKeys, columnKeyFilter);
    valueColumns.add(valueColumn);
    return valueColumn;
  }

  @Override
  protected void initStyles() {
    super.initStyles();
    initKeyColumnsStyles();
    initValueColumnsStyles();
  }

  private void initKeyColumnsStyles() {
    keyColumns.stream().forEach(SimpleColumn::initStyles);
  }

  private void initValueColumnsStyles() {
    valueColumns.stream().forEach(MapColumn::initStyles);
  }

  @Override
  protected void writeHeader() {
    super.writeHeader();
    writeKeyColumnsHeader();
    writeValueColumnsHeader();
  }

  private void writeKeyColumnsHeader() {
    int headerRowCount = getHeaderRowCount();
    for (int i = 0; i < keyColumns.size(); i++)
      keyColumns.get(i).writeHeader(worksheet, captionRowCount, columnIndex(i), headerRowCount);
  }

  private void writeValueColumnsHeader() {
    int headerRowCount = getHeaderRowCount();
    for (int i = 0, columnIndex = columnIndex(keyColumns.size());
        i < valueColumns.size();
        columnIndex += valueColumns.get(i++).getKeys().size())
      valueColumns.get(i).writeHeader(worksheet, captionRowCount, columnIndex, headerRowCount);
  }

  @Override
  protected void writeBody() {
    for (int dataIndex = 0; dataIndex < rowKeys.size(); dataIndex++) writeRecord(dataIndex);
  }

  @Override
  protected void writeRecord(int dataIndex) {
    super.writeRecord(dataIndex);
    writeSequenceColumnToRecord(dataIndex);
    writeKeyColumnsToRecord(dataIndex);
    writeValueColumnsToRecord(dataIndex);
  }

  private void writeSequenceColumnToRecord(int dataIndex) {
    sequenceColumn.writeRecord(
        rowKeys.get(dataIndex), dataIndex, worksheet, toRowIndex(dataIndex), 0);
  }

  private void writeKeyColumnsToRecord(int dataIndex) {
    X x = rowKeys.get(dataIndex);
    int rowIndex = toRowIndex(dataIndex);
    for (int i = 0; i < keyColumns.size(); i++)
      keyColumns.get(i).writeRecord(x, dataIndex, worksheet, rowIndex, columnIndex(i));
  }

  private void writeValueColumnsToRecord(int dataIndex) {
    X x = rowKeys.get(dataIndex);
    int rowIndex = toRowIndex(dataIndex);
    for (int i = 0, columnIndex = columnIndex(keyColumns.size());
        i < valueColumns.size();
        columnIndex += valueColumns.get(i++).getKeys().size())
      valueColumns.get(i).writeRecord(x, data.get(x), dataIndex, worksheet, rowIndex, columnIndex);
  }

  @Override
  protected void configureColumnWidth() {
    super.configureColumnWidth();
    configureKeyColumnsWidth();
    configureValueColumnsWidth();
  }

  private void configureKeyColumnsWidth() {
    for (int i = 0; i < keyColumns.size(); i++)
      keyColumns.get(i).configureColumnWidth(worksheet, columnIndex(i));
  }

  private void configureValueColumnsWidth() {
    for (int i = 0, columnIndex = columnIndex(keyColumns.size());
        i < valueColumns.size();
        columnIndex += valueColumns.get(i++).getKeys().size())
      valueColumns.get(i).configureColumnWidth(worksheet, columnIndex);
  }

  @Override
  protected int calculateColumnsCount() {
    return super.calculateColumnsCount()
        + keyColumns.size()
        + valueColumns.stream().map(MapColumn::getKeys).mapToInt(List::size).sum();
  }

  @Override
  protected int calculateHeaderRowCount() {
    return valueColumns.stream()
            .mapToInt(columnGroup -> columnGroup.getKeyHeaders().size())
            .max()
            .orElse(0)
        + 1;
  }
}
