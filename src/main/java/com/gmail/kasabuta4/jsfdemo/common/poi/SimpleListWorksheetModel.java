package com.gmail.kasabuta4.jsfdemo.common.poi;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SimpleListWorksheetModel<E> {

  // required properties
  private final SimpleListWorkbookModel workbookModel;
  private final String sheetName;
  private final String title;
  private final List<E> data;
  private final List<SimpleListColumnModel<E, E, ?>> columns = new ArrayList<>();

  // optional properties
  private int titleRowIndex = 0;
  private int titleColumnIndex = 0;
  private int titleRows = 1;
  private int titleColumns = 1;
  private int listStartRowIndex = 1;
  private int listStartColumnIndex = 0;
  private SimpleListColumnModel<E, Integer, Integer> sequenceColumn = null;
  private Function<XSSFWorkbook, XSSFCellStyle> titleStyleProducer = this::createTitleStyle;

  // temporary variables used during building workbook
  private XSSFSheet worksheet;
  private XSSFCellStyle titleStyle;

  SimpleListWorksheetModel(
      SimpleListWorkbookModel workbookModel, String sheetName, String title, List<E> data) {
    this.workbookModel = workbookModel;
    this.sheetName = sheetName;
    this.title = title;
    this.data = data;
  }

  public SimpleListWorkbookModel endWorksheet() {
    return workbookModel;
  }

  public SimpleListWorksheetModel<E> titlePosition(int rowIndex, int columnIndex) {
    titleRowIndex = rowIndex;
    titleColumnIndex = columnIndex;
    return this;
  }

  public SimpleListWorksheetModel<E> titleSize(int rows, int columns) {
    titleRows = rows;
    titleColumns = columns;
    return this;
  }

  public SimpleListWorksheetModel<E> listStartPosition(int rowIndex, int columnIndex) {
    listStartRowIndex = rowIndex;
    listStartColumnIndex = columnIndex;
    return this;
  }

  public SimpleListWorksheetModel<E> titleStyle(
      Function<XSSFWorkbook, XSSFCellStyle> titleStyleProducer) {
    this.titleStyleProducer = titleStyleProducer;
    return this;
  }

  public SimpleListColumnModel<E, Integer, Integer> addSequenceColumn(
      String title, int characters, NumberFormat format) {
    return sequenceColumn =
        new SimpleListColumnModel<>(this, title, Function.identity(), characters, format);
  }

  public SimpleListColumnModel<E, E, String> addStringColumn(
      String title, Function<E, String> property, int characters) {
    SimpleListColumnModel<E, E, String> columnModel =
        new SimpleListColumnModel<>(this, title, property, characters, CommonNumberFormat.標準);
    columns.add(columnModel);
    return columnModel;
  }

  public SimpleListColumnModel<E, E, Integer> addIntegerColumn(
      String title, Function<E, Integer> property, int characters, NumberFormat format) {
    SimpleListColumnModel<E, E, Integer> columnModel =
        new SimpleListColumnModel<>(this, title, property, characters, format);
    columns.add(columnModel);
    return columnModel;
  }

  public SimpleListColumnModel<E, E, Double> addDoubleColumn(
      String title, Function<E, Double> property, int characters, NumberFormat format) {
    SimpleListColumnModel<E, E, Double> columnModel =
        new SimpleListColumnModel<>(this, title, property, characters, format);
    columns.add(columnModel);
    return columnModel;
  }

  public SimpleListColumnModel<E, E, YearMonth> addYearMonthColumn(
      String title, Function<E, YearMonth> property) {
    SimpleListColumnModel<E, E, YearMonth> columnModel =
        new SimpleListColumnModel<>(this, title, property, 7, CommonNumberFormat.年月);
    columns.add(columnModel);
    return columnModel;
  }

  XSSFSheet build() {
    initStyles();
    initWorksheet();
    writeTitle();
    writeListHeader();
    writeListData();
    return worksheet;
  }

  private void initStyles() {
    titleStyle = titleStyleProducer.apply(workbookModel.getWorkbook());
    if (sequenceColumn != null) sequenceColumn.initStyles(workbookModel.getWorkbook());
    for (SimpleListColumnModel<E, E, ?> column : columns)
      column.initStyles(workbookModel.getWorkbook());
  }

  private void initWorksheet() {
    worksheet = workbookModel.getWorkbook().createSheet(this.sheetName);
    if (sequenceColumn != null)
      worksheet.setColumnWidth(listStartColumnIndex, sequenceColumn.getColumnWidth());
    for (int i = 0; i < columns.size(); i++)
      worksheet.setColumnWidth(columnIndex(i), columns.get(i).getColumnWidth());
  }

  private void writeTitle() {
    XSSFRow row = worksheet.createRow(titleRowIndex);
    XSSFCell cell = row.createCell(titleColumnIndex);
    cell.setCellStyle(titleStyle);
    XSSFCellUtil.setCellValue(cell, title);
    if (titleRows > 1 || titleColumns > 1)
      worksheet.addMergedRegion(
          new CellRangeAddress(
              titleRowIndex,
              titleRowIndex + titleRows - 1,
              titleColumnIndex,
              titleColumnIndex + titleColumns - 1));
  }

  private void writeListHeader() {
    XSSFRow row = worksheet.createRow(listStartRowIndex);
    if (sequenceColumn != null) sequenceColumn.writeTitle(row.createCell(listStartColumnIndex));
    for (int i = 0; i < columns.size(); i++)
      columns.get(i).writeTitle(row.createCell(columnIndex(i)));
  }

  private void writeListData() {
    for (int i = 0; i < data.size(); i++) {
      E entity = data.get(i);
      XSSFRow row = worksheet.createRow(listStartRowIndex + 1 + i);
      if (sequenceColumn != null)
        sequenceColumn.writeData(i + 1, row.createCell(listStartColumnIndex), i);
      for (int j = 0; j < columns.size(); j++)
        columns.get(j).writeData(entity, row.createCell(columnIndex(j)), i);
    }
  }

  private int columnIndex(int i) {
    return listStartColumnIndex + (sequenceColumn == null ? 0 : 1) + i;
  }

  private XSSFCellStyle createTitleStyle(XSSFWorkbook workbook) {
    XSSFFont standardFont = workbook.getStylesSource().getFontAt(0);

    XSSFFont font = workbook.createFont();
    font.setBold(true);
    font.setColor(standardFont.getColor());
    font.setFontName(standardFont.getFontName());
    font.setFontHeightInPoints((short) (standardFont.getFontHeightInPoints() + 4));

    XSSFCellStyle style = workbook.createCellStyle();
    style.setFont(font);
    return style;
  }
}
