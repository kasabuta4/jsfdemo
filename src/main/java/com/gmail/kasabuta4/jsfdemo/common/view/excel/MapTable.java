package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import static com.gmail.kasabuta4.jsfdemo.common.view.excel.ColumnWidthConfigurators.byCharacters;
import static com.gmail.kasabuta4.jsfdemo.common.view.excel.CommonNumberFormat.年月;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MapTable<X, Y, E> extends AbstractTable<MapTable<X, Y, E>> {

  // required properties
  private final Map<X, Map<Y, E>> data;
  private final List<X> rowKeys;
  private final List<Y> columnKeys;
  private List<SimpleColumn<MapTable<X, Y, E>, X, ?>> keyColumns = new ArrayList<>();
  private List<MapColumn<MapTable<X, Y, E>, Y, E, ?>> valueColumns = new ArrayList<>();

  protected MapTable(
      WorkbookModel workbookModel, String sheetName, String caption, Map<X, Map<Y, E>> data) {
    super(workbookModel, sheetName, caption);
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

  public <V> SimpleColumn<MapTable<X, Y, E>, X, V> addYearMonthKeyColumn(
      String header, Function<X, V> propertyGetter) {
    SimpleColumn<MapTable<X, Y, E>, X, V> keyColumn =
        new SimpleColumn<>(this, header, propertyGetter, byCharacters(7), 年月);
    keyColumns.add(keyColumn);
    return keyColumn;
  }

  public MapColumn<MapTable<X, Y, E>, Y, E, Integer> addIntegerValueColumn(
      String header,
      Predicate<Y> columnKeyFilter,
      Function<E, Integer> propertyGetter,
      ColumnWidthConfigurator columnWidthConfigurator,
      NumberFormat format) {
    MapColumn<MapTable<X, Y, E>, Y, E, Integer> valueColumn =
        new MapColumn<>(
            this,
            header,
            propertyGetter,
            columnWidthConfigurator,
            format,
            columnKeys,
            columnKeyFilter);
    valueColumns.add(valueColumn);
    return valueColumn;
  }

  @Override
  protected void initStyles(XSSFWorkbook workbook) {
    super.initStyles(workbook);
    initKeyColumnsStyles(workbook);
    initValueColumnsStyles(workbook);
  }

  private void initKeyColumnsStyles(XSSFWorkbook workbook) {
    keyColumns.stream().forEach(keyColumn -> keyColumn.initStyles(workbook));
  }

  private void initValueColumnsStyles(XSSFWorkbook workbook) {
    valueColumns.stream().forEach(valueColumn -> valueColumn.initStyles(workbook));
  }

  @Override
  protected void writeHeader() {
    super.writeHeader();
    writeKeyColumnsHeader();
    writeValueColumnsHeader();
  }

  private void writeKeyColumnsHeader() {
    int keyHeaderCount = getHeaderRowCount() - 1;
    for (int i = 0; i < keyColumns.size(); i++)
      keyColumns.get(i).writeHeader(worksheet, headerStartRowIndex, columnIndex(i), keyHeaderCount);
  }

  private void writeValueColumnsHeader() {
    int keyHeaderCount = getHeaderRowCount() - 1;
    for (int i = 0, columnIndex = columnIndex(keyColumns.size());
        i < valueColumns.size();
        columnIndex += valueColumns.get(i++).getKeys().size())
      valueColumns.get(i).writeHeader(worksheet, headerStartRowIndex, columnIndex, keyHeaderCount);
  }

  @Override
  protected void writeBody() {
    for (int dataIndex = 0; dataIndex < rowKeys.size(); dataIndex++) writeRecord(dataIndex);
  }

  @Override
  protected void writeRecord(int dataIndex) {
    super.writeRecord(dataIndex);
    writeKeyColumnsToRecord(dataIndex);
    writeValueColumnsToRecord(dataIndex);
  }

  private void writeKeyColumnsToRecord(int dataIndex) {
    X x = rowKeys.get(dataIndex);
    for (int i = 0; i < keyColumns.size(); i++)
      keyColumns
          .get(i)
          .writeBodyRecord(x, dataIndex, worksheet, toRowIndex(dataIndex), columnIndex(i));
  }

  private void writeValueColumnsToRecord(int dataIndex) {
    X x = rowKeys.get(dataIndex);
    for (int i = 0, columnIndex = columnIndex(keyColumns.size());
        i < valueColumns.size();
        columnIndex += valueColumns.get(i++).getKeys().size())
      valueColumns
          .get(i)
          .writeBodyRecord(data.get(x), dataIndex, worksheet, toRowIndex(dataIndex), columnIndex);
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
  protected int getHeaderRowCount() {
    return valueColumns.stream()
            .mapToInt(columnGroup -> columnGroup.getKeyHeaders().size())
            .max()
            .orElse(0)
        + 1;
  }
}
