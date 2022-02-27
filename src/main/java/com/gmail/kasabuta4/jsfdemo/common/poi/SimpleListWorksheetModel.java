package com.gmail.kasabuta4.jsfdemo.common.poi;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SimpleListWorksheetModel<E> {

  // required property
  private final XSSFWorkbook workbook;
  private final String sheetName;
  private final String title;
  private final List<E> data;
  private final List<SimpleListColumnModel<E, ?>> columns;

  // optional property
  private int titleRowIndex = 0;
  private int titleColumnIndex = 0;
  private int listStartRowIndex = 1;

  // temporary variables used during building workbook
  private XSSFSheet worksheet;
  private XSSFCellStyle titleStyle;

  protected SimpleListWorksheetModel(
      XSSFWorkbook workbook, String sheetName, String title, List<E> data) {
    this.workbook = workbook;
    this.sheetName = sheetName;
    this.title = title;
    this.data = data;
    this.columns = new ArrayList<>();
  }

  public SimpleListWorksheetModel<E> titlePosition(int rowIndex, int columnIndex) {
    titleRowIndex = rowIndex;
    titleColumnIndex = columnIndex;
    return this;
  }

  public SimpleListWorksheetModel<E> listStartRowIndex(int rowIndex) {
    listStartRowIndex = rowIndex;
    return this;
  }

  public SimpleListWorksheetModel<E> addStringColumn(
      int index, String title, int characters, Function<E, String> property) {
    columns.add(
        new SimpleListColumnModel<>(
            this, index, title, characters, CommonNumberFormat.標準, property));
    return this;
  }

  public SimpleListWorksheetModel<E> addIntegerColumn(
      int index, String title, int characters, NumberFormat format, Function<E, Integer> property) {
    columns.add(new SimpleListColumnModel<>(this, index, title, characters, format, property));
    return this;
  }

  public SimpleListWorksheetModel<E> addDoubleColumn(
      int index, String title, int characters, NumberFormat format, Function<E, Double> property) {
    columns.add(new SimpleListColumnModel<>(this, index, title, characters, format, property));
    return this;
  }

  public SimpleListWorksheetModel<E> addYearMonthColumn(
      int index, String title, Function<E, YearMonth> property) {
    columns.add(
        new SimpleListColumnModel<>(this, index, title, 7, CommonNumberFormat.年月, property));
    return this;
  }

  protected void addColumn(SimpleListColumnModel<E, ?> column) {
    columns.add(column);
  }

  protected XSSFSheet build() {
    initStyles();
    initWorksheet();
    writeTitle();
    writeListHeader();
    writeListData();
    return worksheet;
  }

  private void initStyles() {
    titleStyle = createTitleStyle(workbook);
    for (SimpleListColumnModel<E, ?> column : columns) column.initStyles(workbook);
  }

  private void initWorksheet() {
    worksheet = workbook.createSheet(this.sheetName);
    for (SimpleListColumnModel<E, ?> column : columns) column.setColumnWidth(worksheet);
  }

  private void writeTitle() {
    XSSFRow row = worksheet.createRow(titleRowIndex);
    XSSFCell cell = row.createCell(titleColumnIndex);
    cell.setCellStyle(titleStyle);
    XSSFCellUtil.setCellValue(cell, title);
  }

  private void writeListHeader() {
    XSSFRow row = worksheet.createRow(listStartRowIndex);
    for (SimpleListColumnModel<E, ?> column : columns) column.writeTitle(row);
  }

  private void writeListData() {
    for (int i = 0; i < data.size(); i++) {
      E entity = data.get(i);
      XSSFRow row = worksheet.createRow(listStartRowIndex + 1 + i);
      for (SimpleListColumnModel<E, ?> column : columns) column.writeData(i, entity, row);
    }
  }

  protected XSSFCellStyle createTitleStyle(XSSFWorkbook workbook) {
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
