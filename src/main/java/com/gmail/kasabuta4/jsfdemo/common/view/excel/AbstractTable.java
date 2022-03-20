package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Function;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class AbstractTable<T extends AbstractTable> {

  // required properties
  protected final WorkbookModel workbookModel;
  private final String sheetName;

  // optional properties
  private String headerStyleKey = "header";
  private List<String> bodyStyleKeys;
  private Caption<T> caption = null;
  private SimpleColumn<T, Integer, Integer> sequenceColumn = null;

  // temporary variables used during building workbook
  protected int captionRowCount;
  private int columnsCount;
  private int headerRowCount = 1;
  protected XSSFSheet worksheet;
  XSSFCellStyle headerStyle;
  List<XSSFCellStyle> bodyStyles;

  protected AbstractTable(WorkbookModel workbookModel, String sheetName) {
    this.workbookModel = workbookModel;
    this.sheetName = sheetName;
  }

  protected abstract T self();

  public WorkbookModel endTable() {
    return workbookModel;
  }

  public T headerStyleKey(String headerStyleKey) {
    this.headerStyleKey = headerStyleKey;
    return self();
  }

  public T bodyStyleKeys(List<String> bodyStyleKeys) {
    this.bodyStyleKeys = bodyStyleKeys;
    return self();
  }

  public Caption<T> addCaption(String caption) {
    return this.caption = new Caption<>(self(), caption);
  }

  public SimpleColumn<T, Integer, Integer> addSequenceColumn(
      String header, ColumnWidth columnWidthConfigurator) {
    return sequenceColumn =
        new SimpleColumn<>(self(), header, Function.identity(), columnWidthConfigurator);
  }

  public XSSFSheet build(XSSFWorkbook workbook) {
    initCaptionRowCount();
    initColumnsCount();
    initHeaderRowCount();
    initStyles();
    initWorksheet(workbook);
    writeCaption();
    writeHeader();
    writeBody();
    configureColumnWidth();
    return worksheet;
  }

  protected WorkbookModel getWorkbookModel() {
    return workbookModel;
  }

  private void initCaptionRowCount() {
    captionRowCount = caption == null ? 0 : caption.rowCount();
  }

  private void initColumnsCount() {
    columnsCount = calculateColumnsCount();
  }

  private void initHeaderRowCount() {
    headerRowCount = calculateHeaderRowCount();
  }

  protected void initStyles() {
    headerStyle = workbookModel.styleOf(headerStyleKey);
    bodyStyles = bodyStyleKeys.stream().map(workbookModel::styleOf).collect(toList());
    if (caption != null) caption.initStyles();
    if (sequenceColumn != null) sequenceColumn.initStyles();
  }

  protected void initWorksheet(XSSFWorkbook workbook) {
    worksheet = workbook.createSheet(sheetName);
  }

  protected void writeCaption() {
    if (caption != null) caption.writeCaption(worksheet);
  }

  protected void writeHeader() {
    createHeaderRows();
    writeSequenceColumnHeader();
  }

  private void createHeaderRows() {
    for (int i = 0; i < getHeaderRowCount(); i++) worksheet.createRow(captionRowCount + i);
  }

  private void writeSequenceColumnHeader() {
    if (sequenceColumn != null)
      sequenceColumn.writeHeader(worksheet, captionRowCount, 0, getHeaderRowCount());
  }

  protected abstract void writeBody();

  protected void writeRecord(int dataIndex) {
    createRowForRecord(dataIndex);
    writeSequenceColumnToRecord(dataIndex);
  }

  private void createRowForRecord(int dataIndex) {
    worksheet.createRow(toRowIndex(dataIndex));
  }

  private void writeSequenceColumnToRecord(int dataIndex) {
    if (sequenceColumn != null)
      sequenceColumn.writeRecord(dataIndex + 1, dataIndex, worksheet, toRowIndex(dataIndex), 0);
  }

  protected void configureColumnWidth() {
    configureSequenceColumnWidth();
  }

  private void configureSequenceColumnWidth() {
    if (sequenceColumn != null) sequenceColumn.configureColumnWidth(worksheet, 0);
  }

  protected int calculateColumnsCount() {
    return sequenceColumn == null ? 0 : 1;
  }

  int getColumnsCount() {
    return columnsCount;
  }

  protected int calculateHeaderRowCount() {
    return 1;
  }

  protected int getHeaderRowCount() {
    return headerRowCount;
  }

  protected int toRowIndex(int dataIndex) {
    return captionRowCount + getHeaderRowCount() + dataIndex;
  }

  protected int columnIndex(int i) {
    return (sequenceColumn == null ? 0 : 1) + i;
  }
}
