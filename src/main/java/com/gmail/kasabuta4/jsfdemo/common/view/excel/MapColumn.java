package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class MapColumn<T extends AbstractTable, X, Y, E, V>
    extends AbstractColumn<MapColumn<T, X, Y, E, V>, T, E, V, X> {

  // required properties
  private final List<Y> keys;
  private final List<KeyHeader<MapColumn<T, X, Y, E, V>, Y, ?>> keyHeaders = new ArrayList<>();

  protected MapColumn(
      T table,
      String header,
      Function<E, V> propertyGetter,
      ColumnWidth columnWidthConfigurator,
      List<Y> keys,
      Predicate<Y> keyFilter) {
    super(table, header, propertyGetter, columnWidthConfigurator);
    this.keys = keys.stream().filter(keyFilter).collect(toList());
  }

  @Override
  protected MapColumn<T, X, Y, E, V> self() {
    return this;
  }

  public KeyHeader<MapColumn<T, X, Y, E, V>, Y, Y> addIdentityKeyHeader() {
    return addKeyHeader(Function.identity());
  }

  public <P> KeyHeader<MapColumn<T, X, Y, E, V>, Y, P> addKeyHeader(Function<Y, P> propertyGetter) {
    KeyHeader<MapColumn<T, X, Y, E, V>, Y, P> keyHeader = new KeyHeader<>(this, propertyGetter);
    keyHeaders.add(keyHeader);
    return keyHeader;
  }

  @Override
  protected void writeHeader(XSSFSheet sheet, int rowIndex, int columnIndex, int headerRowCount) {
    if (!keys.isEmpty()) {
      writeMapColumnHeader(sheet, rowIndex, columnIndex);
      writeKeyHeader(sheet, rowIndex + 1, columnIndex);
      mergeKeyHeaderCell(sheet, rowIndex + 1, columnIndex, headerRowCount);
    }
  }

  private void writeMapColumnHeader(XSSFSheet sheet, int rowIndex, int columnIndex) {
    XSSFRow headerRow = sheet.getRow(rowIndex);
    XSSFCell headerCell = headerRow.createCell(columnIndex);
    XSSFCellStyle style =
        getHeaderStyleKey() == null ? null : getWorkbookModel().styleOf(getHeaderStyleKey());
    if (style != null) headerCell.setCellStyle(style);
    Cells.setCellValue(headerCell, header);
    Cells.mergeCell(headerCell, 1, keys.size());
  }

  private void writeKeyHeader(XSSFSheet sheet, int rowIndex, int columnIndex) {
    for (int i = 0; i < keyHeaders.size(); i++)
      keyHeaders.get(i).writeKeyHeader(keys, sheet, rowIndex + i, columnIndex);
  }

  private void mergeKeyHeaderCell(
      XSSFSheet sheet, int rowIndex, int columnIndex, int headerRowCount) {
    for (int i = 0; i < keys.size(); i++) {
      XSSFCell cell = sheet.getRow(rowIndex + keyHeaders.size() - 1).getCell(columnIndex + i);
      Cells.mergeCell(cell, headerRowCount - keyHeaders.size(), 1);
    }
  }

  void writeRecord(
      X rowKey, Map<Y, E> value, int dataIndex, XSSFSheet sheet, int rowIndex, int columnIndex) {
    XSSFCellStyle style = findStyle(rowKey, dataIndex);
    for (int i = 0; i < keys.size(); i++) {
      XSSFCell cell = sheet.getRow(rowIndex).createCell(columnIndex + i);
      if (style != null) cell.setCellStyle(style);
      Cells.setCellValue(cell, property(value.get(keys.get(i))));
    }
  }

  List<Y> getKeys() {
    return keys;
  }

  List<KeyHeader<MapColumn<T, X, Y, E, V>, Y, ?>> getKeyHeaders() {
    return keyHeaders;
  }
}
