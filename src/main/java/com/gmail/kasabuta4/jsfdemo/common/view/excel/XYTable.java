package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import static com.gmail.kasabuta4.jsfdemo.common.view.excel.ColumnWidthConfigurators.byCharacters;
import static com.gmail.kasabuta4.jsfdemo.common.view.excel.CommonNumberFormat.年月;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XYTable<X, Y, E> implements WorkSheetModel {

  // required properties
  private final WorkbookModel workbookModel;
  private final String sheetName;
  private final String caption;
  private final Map<X, Map<Y, E>> data;
  private final List<X> xList;
  private final List<Y> yList;
  private List<XColumn<X, Y, E, ?>> xColumns = new ArrayList<>();
  private List<YColumn<X, Y, E, ?>> yColumns = new ArrayList<>();

  // optional properties
  private int captionRowIndex = 0;
  private int captionColumnIndex = 0;
  private int captionRows = 1;
  private int captionColumns = 1;
  private int headerStartRowIndex = 1;
  private int headerStartColumnIndex = 0;
  private Stylers.Simple captionStyler = Stylers.forTableCaption();

  // temporary variables used during building workbook
  private XSSFSheet worksheet;
  private XSSFCellStyle captionStyle;

  XYTable(WorkbookModel workbookModel, String sheetName, String caption, Map<X, Map<Y, E>> data) {
    this.workbookModel = workbookModel;
    this.sheetName = sheetName;
    this.caption = caption;
    this.data = data;
    this.xList = data.keySet().stream().sorted().collect(toList());
    this.yList =
        data.values().stream()
            .flatMap(map -> map.keySet().stream())
            .distinct()
            .sorted()
            .collect(toList());
  }

  public WorkbookModel endXYTable() {
    return workbookModel;
  }

  public XYTable<X, Y, E> captionPosition(int rowIndex, int columnIndex) {
    captionRowIndex = rowIndex;
    captionColumnIndex = columnIndex;
    return this;
  }

  public XYTable<X, Y, E> captionSize(int rows, int columns) {
    captionRows = rows;
    captionColumns = columns;
    return this;
  }

  public XYTable<X, Y, E> headerStartPosition(int rowIndex, int columnIndex) {
    headerStartRowIndex = rowIndex;
    headerStartColumnIndex = columnIndex;
    return this;
  }

  public XYTable<X, Y, E> captionStyler(Stylers.Simple captionStyler) {
    this.captionStyler = captionStyler;
    return this;
  }

  public <V> XColumn<X, Y, E, V> addYearMonthXColumn(String header, Function<X, V> propertyGetter) {
    XColumn<X, Y, E, V> xColumn = new XColumn<>(this, header, propertyGetter, byCharacters(7), 年月);
    xColumns.add(xColumn);
    return xColumn;
  }

  public YColumn<X, Y, E, Integer> addIntegerYColumn(
      String header,
      Predicate<Y> yFilter,
      Function<E, Integer> propertyGetter,
      ColumnWidthConfigurator columnWidthConfigurator,
      NumberFormat format) {
    YColumn<X, Y, E, Integer> yColumn =
        new YColumn<>(
            this, yList, header, yFilter, propertyGetter, columnWidthConfigurator, format);
    yColumns.add(yColumn);
    return yColumn;
  }

  public XSSFSheet build(XSSFWorkbook workbook) {
    initStyles(workbook);
    initWorksheet(workbook);
    writeTableCaption();
    writeTableHeader();
    writeTableBody();
    configureColumnWidth();
    return worksheet;
  }

  private void initStyles(XSSFWorkbook workbook) {
    captionStyle = captionStyler.createStyle(workbook);
    for (XColumn<X, Y, E, ?> xColumn : xColumns) xColumn.initStyles(workbook);
    for (YColumn<X, Y, E, ?> yColumn : yColumns) yColumn.initStyles(workbook);
  }

  private void initWorksheet(XSSFWorkbook workbook) {
    worksheet = workbook.createSheet(sheetName);
  }

  private void writeTableCaption() {
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

  private void writeTableHeader() {
    int maxYTitles = maxYTitles();

    XSSFRow row = worksheet.createRow(headerStartRowIndex);

    for (int i = 0; i < xColumns.size(); i++)
      xColumns
          .get(i)
          .writeHeader(worksheet, headerStartRowIndex, headerStartColumnIndex + i, maxYTitles);

    for (int i = 0, columnIndex = headerStartColumnIndex + xColumns.size();
        i < yColumns.size();
        columnIndex += yColumns.get(i++).getYList().size())
      yColumns.get(i).writeHeader(worksheet, headerStartRowIndex, columnIndex, maxYTitles);
  }

  private void writeTableBody() {
    int maxYTitles = maxYTitles();

    for (int xIndex = 0; xIndex < xList.size(); xIndex++) {
      X x = xList.get(xIndex);
      XSSFRow row = worksheet.createRow(headerStartRowIndex + 1 + maxYTitles + xIndex);

      for (int i = 0; i < xColumns.size(); i++) {
        XSSFCell cell = row.createCell(headerStartColumnIndex + i);
        xColumns.get(i).writeBody(x, cell, xIndex);
      }

      for (int i = 0, columnIndex = headerStartColumnIndex + xColumns.size();
          i < yColumns.size();
          columnIndex += yColumns.get(i++).getYList().size())
        yColumns.get(i).writeBody(data.get(x), row, columnIndex, xIndex);
    }
  }

  private void configureColumnWidth() {
    for (int i = 0; i < xColumns.size(); i++)
      xColumns.get(i).configureColumnWidth(worksheet, headerStartColumnIndex + i);
    for (int i = 0, columnIndex = headerStartColumnIndex + xColumns.size();
        i < yColumns.size();
        columnIndex += yColumns.get(i++).getYList().size())
      yColumns.get(i).configureColumnWidth(worksheet, columnIndex);
  }

  private int maxYTitles() {
    return yColumns.stream()
        .mapToInt(columnGroup -> columnGroup.getYTitles().size())
        .max()
        .orElse(0);
  }
}
