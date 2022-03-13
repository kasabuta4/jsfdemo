package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MapColumn<T extends WorkSheetModel, Y, E, V>
    extends AbstractColumn<MapColumn<T, Y, E, V>, T, E, V> {

  // required properties
  private final List<Y> keys;
  private final List<KeyHeader<MapColumn<T, Y, E, V>, Y, ?>> keyHeaders = new ArrayList<>();

  protected MapColumn(
      T table,
      String header,
      Function<E, V> propertyGetter,
      ColumnWidthConfigurator columnWidthConfigurator,
      NumberFormat format,
      List<Y> keys,
      Predicate<Y> keyFilter) {
    super(table, header, propertyGetter, columnWidthConfigurator, format);
    this.keys = keys.stream().filter(keyFilter).collect(toList());
  }

  @Override
  protected MapColumn<T, Y, E, V> self() {
    return this;
  }

  public KeyHeader<MapColumn<T, Y, E, V>, Y, Y> addIdentityKeyHeader(NumberFormat format) {
    return addKeyHeader(Function.identity(), format);
  }

  public <P> KeyHeader<MapColumn<T, Y, E, V>, Y, P> addKeyHeader(
      Function<Y, P> propertyGetter, NumberFormat format) {
    KeyHeader<MapColumn<T, Y, E, V>, Y, P> keyHeader =
        new KeyHeader<>(this, propertyGetter, format);
    keyHeaders.add(keyHeader);
    return keyHeader;
  }

  @Override
  protected void initStyles(XSSFWorkbook workbook) {
    super.initStyles(workbook);
    keyHeaders.stream().forEach(yTitle -> yTitle.initStyles(workbook));
  }

  @Override
  protected void writeHeader(XSSFSheet sheet, int rowIndex, int columnIndex, int headerRowCount) {
    if (!keys.isEmpty()) {
      writeMapColumnHeader(sheet, rowIndex, columnIndex);
      writeKeyHeader(sheet, rowIndex, columnIndex);
      mergeKeyHeaderCell(sheet, rowIndex, columnIndex, headerRowCount);
    }
  }

  private void writeMapColumnHeader(XSSFSheet sheet, int rowIndex, int columnIndex) {
    XSSFRow headerRow = sheet.getRow(rowIndex);
    XSSFCell headerCell = headerRow.createCell(columnIndex);
    headerCell.setCellStyle(headerStyle);
    XSSFCellUtil.setCellValue(headerCell, header);
    XSSFCellUtil.mergeCell(headerCell, 1, keys.size());
  }

  private void writeKeyHeader(XSSFSheet sheet, int rowIndex, int columnIndex) {
    for (int i = 0; i < keyHeaders.size(); i++)
      keyHeaders.get(i).writeKeyHeader(keys, sheet, rowIndex + 1 + i, columnIndex);
  }

  private void mergeKeyHeaderCell(
      XSSFSheet sheet, int rowIndex, int columnIndex, int headerRowCount) {
    for (int i = 0; i < keys.size(); i++) {
      XSSFCell cell = sheet.getRow(rowIndex + keyHeaders.size()).getCell(columnIndex + i);
      XSSFCellUtil.mergeCell(cell, headerRowCount - keyHeaders.size(), 1);
    }
  }

  protected void writeRecord(
      Map<Y, E> value, int dataIndex, XSSFSheet sheet, int rowIndex, int columnIndex) {
    for (int i = 0; i < keys.size(); i++) {
      XSSFCell cell = sheet.getRow(rowIndex).createCell(columnIndex + i);
      cell.setCellStyle(bodyStyles.get(dataIndex % bodyStyles.size()));
      XSSFCellUtil.setCellValue(cell, property(value.get(keys.get(i))));
    }
  }

  protected List<Y> getKeys() {
    return keys;
  }

  protected List<KeyHeader<MapColumn<T, Y, E, V>, Y, ?>> getKeyHeaders() {
    return keyHeaders;
  }
}
