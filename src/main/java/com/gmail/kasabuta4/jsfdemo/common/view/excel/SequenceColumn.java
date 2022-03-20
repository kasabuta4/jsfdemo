package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import java.util.function.Function;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class SequenceColumn<T extends AbstractTable, E>
    extends AbstractColumn<SequenceColumn<T, E>, T, E, E, E> {

  protected SequenceColumn(T table, String header, ColumnWidth columnWidthConfigurator) {
    super(table, header, Function.identity(), columnWidthConfigurator);
  }

  @Override
  protected SequenceColumn<T, E> self() {
    return this;
  }

  protected void writeRecord(
      E entity, int dataIndex, XSSFSheet sheet, int rowIndex, int columnIndex) {
    XSSFCell cell = sheet.getRow(rowIndex).createCell(columnIndex);
    XSSFCellStyle style = findStyle(entity, dataIndex);
    if (style != null) cell.setCellStyle(style);
    Cells.setCellValue(cell, dataIndex + 1);
  }
}
