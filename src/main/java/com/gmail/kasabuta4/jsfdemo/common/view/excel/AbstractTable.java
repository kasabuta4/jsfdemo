package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import java.util.List;
import java.util.function.Function;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class AbstractTable<T extends AbstractTable, E> {

  // required properties
  protected final WorkbookModel workbookModel;
  private final String sheetName;

  // optional properties
  private Caption<T> caption = null;
  protected SequenceColumn<T, E> sequenceColumn = null;
  private String headerStyleKey = "header";
  private List<String> bodyStyleKeys;
  private Function<E, Object> highlightPropertyGetter;
  private Function<Object, List<String>> highlightStyleKeyConverter;

  // temporary variables used during building workbook
  protected int captionRowCount;
  private int columnsCount;
  private int headerRowCount = 1;
  protected XSSFSheet worksheet;

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

  public T highlight(Function<E, Object> propertyGetter, Function<Object, List<String>> converter) {
    this.highlightPropertyGetter = propertyGetter;
    this.highlightStyleKeyConverter = converter;
    return self();
  }

  public T highlightPropertyGetter(Function<E, Object> propertyGetter) {
    this.highlightPropertyGetter = propertyGetter;
    return self();
  }

  public Caption<T> addCaption(String caption) {
    return this.caption = new Caption<>(self(), caption);
  }

  public SequenceColumn<T, E> addSequenceColumn(
      String header, ColumnWidth columnWidthConfigurator) {
    return sequenceColumn = new SequenceColumn<>(self(), header, columnWidthConfigurator);
  }

  public XSSFSheet build(XSSFWorkbook workbook) {
    initCaptionRowCount();
    initColumnsCount();
    initHeaderRowCount();
    initWorksheet(workbook);
    writeCaption();
    writeHeader();
    writeBody();
    configureColumnWidth();
    return worksheet;
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

  private void initWorksheet(XSSFWorkbook workbook) {
    worksheet = workbook.createSheet(sheetName);
  }

  private void writeCaption() {
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
  }

  private void createRowForRecord(int dataIndex) {
    worksheet.createRow(toRowIndex(dataIndex));
  }

  protected void configureColumnWidth() {
    configureSequenceColumnWidth();
  }

  private void configureSequenceColumnWidth() {
    if (sequenceColumn != null) sequenceColumn.configureColumnWidth(worksheet, 0);
  }

  WorkbookModel getWorkbookModel() {
    return workbookModel;
  }

  String getHeaderStyleKey() {
    return headerStyleKey;
  }

  List<String> getBodyStyleKeys() {
    return bodyStyleKeys;
  }

  Function<E, Object> getHighlightPropertyGetter() {
    return highlightPropertyGetter;
  }

  Function<Object, List<String>> getHighlightStyleKeyConverter() {
    return highlightStyleKeyConverter;
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
