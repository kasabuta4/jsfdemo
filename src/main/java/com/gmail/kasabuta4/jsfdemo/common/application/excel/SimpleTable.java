package com.gmail.kasabuta4.jsfdemo.common.application.excel;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class SimpleTable<E> implements WorkSheetModel {

  // required properties
  private final WorkbookModel workbookModel;
  private final String sheetName;
  private final String caption;
  private final List<E> data;
  private final List<SimpleColumn<E, E, ?>> columns = new ArrayList<>();

  // optional properties
  private int captionRowIndex = 0;
  private int captionColumnIndex = 0;
  private int captionRows = 1;
  private int captionColumns = 1;
  private int headerStartRowIndex = 1;
  private int headerStartColumnIndex = 0;
  private SimpleColumn<E, Integer, Integer> sequenceColumn = null;
  private Stylers.Simple captionStyler = Stylers.forTableCaption();

  // temporary variables used during building workbook
  private XSSFSheet worksheet;
  private XSSFCellStyle captionStyle;

  SimpleTable(WorkbookModel workbookModel, String sheetName, String caption, List<E> data) {
    this.workbookModel = workbookModel;
    this.sheetName = sheetName;
    this.caption = caption;
    this.data = data;
  }

  public WorkbookModel endSimpleTable() {
    return workbookModel;
  }

  public SimpleTable<E> captionPosition(int rowIndex, int columnIndex) {
    captionRowIndex = rowIndex;
    captionColumnIndex = columnIndex;
    return this;
  }

  public SimpleTable<E> captionSize(int rows, int columns) {
    captionRows = rows;
    captionColumns = columns;
    return this;
  }

  public SimpleTable<E> headerStartPosition(int rowIndex, int columnIndex) {
    headerStartRowIndex = rowIndex;
    headerStartColumnIndex = columnIndex;
    return this;
  }

  public SimpleTable<E> captionStyler(Stylers.Simple captionStyler) {
    this.captionStyler = captionStyler;
    return this;
  }

  public SimpleColumn<E, Integer, Integer> addSequenceColumn(
      String header, int characters, NumberFormat format) {
    return sequenceColumn =
        new SimpleColumn<>(this, header, Function.identity(), characters, format);
  }

  public SimpleColumn<E, E, String> addStringColumn(
      String header, Function<E, String> property, int characters) {
    SimpleColumn<E, E, String> columnModel =
        new SimpleColumn<>(this, header, property, characters, CommonNumberFormat.標準);
    columns.add(columnModel);
    return columnModel;
  }

  public SimpleColumn<E, E, Integer> addIntegerColumn(
      String header, Function<E, Integer> property, int characters, NumberFormat format) {
    SimpleColumn<E, E, Integer> columnModel =
        new SimpleColumn<>(this, header, property, characters, format);
    columns.add(columnModel);
    return columnModel;
  }

  public SimpleColumn<E, E, Double> addDoubleColumn(
      String header, Function<E, Double> property, int characters, NumberFormat format) {
    SimpleColumn<E, E, Double> columnModel =
        new SimpleColumn<>(this, header, property, characters, format);
    columns.add(columnModel);
    return columnModel;
  }

  public SimpleColumn<E, E, YearMonth> addYearMonthColumn(
      String header, Function<E, YearMonth> property) {
    SimpleColumn<E, E, YearMonth> columnModel =
        new SimpleColumn<>(this, header, property, 7, CommonNumberFormat.年月);
    columns.add(columnModel);
    return columnModel;
  }

  public XSSFSheet build() {
    initStyles();
    initWorksheet();
    writeCaption();
    writeHeader();
    writeBody();
    return worksheet;
  }

  private void initStyles() {
    captionStyle = captionStyler.createStyle(workbookModel.getWorkbook());
    if (sequenceColumn != null) sequenceColumn.initStyles(workbookModel.getWorkbook());
    for (SimpleColumn<E, E, ?> column : columns) column.initStyles(workbookModel.getWorkbook());
  }

  private void initWorksheet() {
    worksheet = workbookModel.getWorkbook().createSheet(this.sheetName);
    if (sequenceColumn != null)
      worksheet.setColumnWidth(headerStartColumnIndex, sequenceColumn.getColumnWidth());
    for (int i = 0; i < columns.size(); i++)
      worksheet.setColumnWidth(columnIndex(i), columns.get(i).getColumnWidth());
  }

  private void writeCaption() {
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

  private void writeHeader() {
    XSSFRow row = worksheet.createRow(headerStartRowIndex);
    if (sequenceColumn != null) sequenceColumn.writeHeader(row.createCell(headerStartColumnIndex));
    for (int i = 0; i < columns.size(); i++)
      columns.get(i).writeHeader(row.createCell(columnIndex(i)));
  }

  private void writeBody() {
    for (int i = 0; i < data.size(); i++) {
      E entity = data.get(i);
      XSSFRow row = worksheet.createRow(headerStartRowIndex + 1 + i);
      if (sequenceColumn != null)
        sequenceColumn.writeBody(i + 1, row.createCell(headerStartColumnIndex), i);
      for (int j = 0; j < columns.size(); j++)
        columns.get(j).writeBody(entity, row.createCell(columnIndex(j)), i);
    }
  }

  private int columnIndex(int i) {
    return headerStartColumnIndex + (sequenceColumn == null ? 0 : 1) + i;
  }
}
