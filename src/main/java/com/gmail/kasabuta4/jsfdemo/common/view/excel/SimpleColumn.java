package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import java.util.function.Function;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class SimpleColumn<T extends AbstractTable, E, V>
    extends AbstractColumn<SimpleColumn<T, E, V>, T, E, V, E> {

  protected SimpleColumn(
      T table, String header, Function<E, V> property, ColumnWidth columnWidthConfigurator) {
    super(table, header, property, columnWidthConfigurator);
  }

  @Override
  protected SimpleColumn<T, E, V> self() {
    return this;
  }

  void writeRecord(E entity, int dataIndex, XSSFSheet sheet, int rowIndex, int columnIndex) {
    XSSFCell cell = sheet.getRow(rowIndex).createCell(columnIndex);
    XSSFCellStyle style = findStyle(entity, dataIndex);
    if (style != null) cell.setCellStyle(style);
    Cells.setCellValue(cell, property(entity));
  }
}
