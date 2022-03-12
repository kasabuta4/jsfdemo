package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import java.util.function.Function;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class AbstractTable<T extends AbstractTable> implements WorkSheetModel {

  // required properties
  private final WorkbookModel workbookModel;
  private final String sheetName;
  private final String caption;

  // optional properties
  private int captionRowIndex = 0;
  private int captionColumnIndex = 0;
  private int captionRows = 1;
  private int captionColumns = 1;
  protected int headerStartRowIndex = 1;
  protected int headerStartColumnIndex = 0;
  private Stylers.Simple captionStyler = Stylers.forTableCaption();
  protected SimpleColumn<T, Integer, Integer> sequenceColumn = null;

  // temporary variables used during building workbook
  protected XSSFSheet worksheet;
  private XSSFCellStyle captionStyle;

  protected AbstractTable(WorkbookModel workbookModel, String sheetName, String caption) {
    this.workbookModel = workbookModel;
    this.sheetName = sheetName;
    this.caption = caption;
  }

  protected abstract T self();

  public WorkbookModel endTable() {
    return workbookModel;
  }

  public T captionPosition(int rowIndex, int columnIndex) {
    captionRowIndex = rowIndex;
    captionColumnIndex = columnIndex;
    return self();
  }

  public T captionSize(int rows, int columns) {
    captionRows = rows;
    captionColumns = columns;
    return self();
  }

  public T headerStartPosition(int rowIndex, int columnIndex) {
    headerStartRowIndex = rowIndex;
    headerStartColumnIndex = columnIndex;
    return self();
  }

  public T captionStyler(Stylers.Simple captionStyler) {
    this.captionStyler = captionStyler;
    return self();
  }

  public SimpleColumn<T, Integer, Integer> addSequenceColumn(
      String header, ColumnWidthConfigurator columnWidthConfigurator, NumberFormat format) {
    return sequenceColumn =
        new SimpleColumn<>(self(), header, Function.identity(), columnWidthConfigurator, format);
  }

  public XSSFSheet build(XSSFWorkbook workbook) {
    initStyles(workbook);
    initWorksheet(workbook);
    writeCaption();
    writeHeader();
    writeBody();
    configureColumnWidth();
    return worksheet;
  }

  protected void initStyles(XSSFWorkbook workbook) {
    initCaptionStyle(workbook);
    initSequenceColumnStyles(workbook);
  }

  private void initCaptionStyle(XSSFWorkbook workbook) {
    captionStyle = captionStyler.createStyle(workbook);
  }

  private void initSequenceColumnStyles(XSSFWorkbook workbook) {
    if (sequenceColumn != null) sequenceColumn.initStyles(workbook);
  }

  protected void initWorksheet(XSSFWorkbook workbook) {
    worksheet = workbook.createSheet(sheetName);
  }

  protected void writeCaption() {
    XSSFRow row = worksheet.createRow(captionRowIndex);
    XSSFCell cell = row.createCell(captionColumnIndex);
    cell.setCellStyle(captionStyle);
    XSSFCellUtil.setCellValue(cell, caption);
    if (captionRows > 1 || captionColumns > 1)
      worksheet.addMergedRegion(
          new CellRangeAddress(
              captionRowIndex,
              captionRowIndex + captionRows - 1,
              captionColumnIndex,
              captionColumnIndex + captionColumns - 1));
  }

  protected void writeHeader() {
    createHeaderRows();
    writeSequenceColumnHeader();
  }

  private void createHeaderRows() {
    for (int i = 0; i < getHeaderRowCount(); i++) worksheet.createRow(headerStartRowIndex + i);
  }

  private void writeSequenceColumnHeader() {
    if (sequenceColumn != null)
      sequenceColumn.writeHeader(
          worksheet, headerStartRowIndex, headerStartColumnIndex, getHeaderRowCount() - 1);
  }

  protected abstract void writeBody();

  protected void writeRecord(int dataIndex) {
    createRowForRecord(dataIndex);
    writeSequenceColumnToRecord(dataIndex);
  }

  private void createRowForRecord(int dataIndex) {
    worksheet.createRow(headerStartRowIndex + getHeaderRowCount() + dataIndex);
  }

  private void writeSequenceColumnToRecord(int dataIndex) {
    if (sequenceColumn != null)
      sequenceColumn.writeBodyRecord(
          dataIndex + 1, dataIndex, worksheet, toRowIndex(dataIndex), headerStartColumnIndex);
  }

  protected void configureColumnWidth() {
    configureSequenceColumnWidth();
  }

  private void configureSequenceColumnWidth() {
    if (sequenceColumn != null)
      sequenceColumn.configureColumnWidth(worksheet, headerStartColumnIndex);
  }

  protected int getHeaderRowCount() {
    return 1;
  }

  protected int toRowIndex(int dataIndex) {
    return headerStartRowIndex + getHeaderRowCount() + dataIndex;
  }

  protected int columnIndex(int i) {
    return headerStartColumnIndex + (sequenceColumn == null ? 0 : 1) + i;
  }
}
