package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class Caption<T extends AbstractTable> {

  // required properties
  private final T table;
  private final String caption;

  // optional properties
  private String captionStyleKey = "caption";
  private boolean cellMerged = true;
  private boolean hasBlankRow = true;

  // temporary variables used during building workbook
  XSSFCellStyle captionStyle;

  Caption(T table, String caption) {
    this.table = table;
    this.caption = caption;
  }

  public T endCaption() {
    return table;
  }

  public Caption<T> captionStyleKey(String captionStyleKey) {
    this.captionStyleKey = captionStyleKey;
    return this;
  }

  public Caption<T> cellMerged(boolean cellMerged) {
    this.cellMerged = cellMerged;
    return this;
  }

  public Caption<T> hasBlankRow(boolean hasBlankRow) {
    this.hasBlankRow = hasBlankRow;
    return this;
  }

  void initStyles() {
    captionStyle = getWorkbookModel().styleOf(captionStyleKey);
  }

  void writeCaption(XSSFSheet worksheet) {
    XSSFRow row = worksheet.createRow(0);
    XSSFCell cell = row.createCell(0);
    cell.setCellStyle(captionStyle);
    Cells.setCellValue(cell, caption);
    if (cellMerged) Cells.mergeCell(cell, 1, table.getColumnsCount());
  }

  int rowCount() {
    return hasBlankRow ? 2 : 1;
  }

  private WorkbookModel getWorkbookModel() {
    return table.getWorkbookModel();
  }
}
