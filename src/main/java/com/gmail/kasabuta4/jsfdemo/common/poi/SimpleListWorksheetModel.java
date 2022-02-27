package com.gmail.kasabuta4.jsfdemo.common.poi;

import java.awt.Color;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SimpleListWorksheetModel<E> {

  private static final Color FILL_COLOR_FOR_EVEN_ROW = new Color(224, 255, 255);
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

  // build result
  private XSSFSheet worksheet;
  private XSSFCellStyle titleStyle;
  private Map<SimpleListColumnModel<E, ?>, XSSFCellStyle> listHeaderStyles;
  private List<Map<SimpleListColumnModel<E, ?>, XSSFCellStyle>> listDataStyles;

  public SimpleListWorksheetModel(
      XSSFWorkbook workbook, String sheetName, String title, List<E> data) {
    this.workbook = workbook;
    this.sheetName = sheetName;
    this.title = title;
    this.data = data;
    this.columns = new ArrayList<>();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    if (!(o instanceof SimpleListWorksheetModel)) return false;
    final SimpleListWorksheetModel<?> other = (SimpleListWorksheetModel<?>) o;
    return Objects.equals(sheetName, other.sheetName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sheetName);
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

  public XSSFSheet build() {
    initStyles();
    initWorksheet();
    writeTitle();
    writeListHeader();
    writeListData();
    return worksheet;
  }

  private void initStyles() {
    titleStyle = createTitleStyle();
    listHeaderStyles = createListHeaderStyles();
    listDataStyles = createListDataStyles();
  }

  private void initWorksheet() {
    worksheet = workbook.createSheet(this.sheetName);
    columns.stream()
        .forEach(column -> worksheet.setColumnWidth(column.getIndex(), column.getColumnWidth()));
  }

  private void writeTitle() {
    XSSFRow row = worksheet.createRow(titleRowIndex);
    XSSFCell cell = row.createCell(titleColumnIndex);
    cell.setCellStyle(titleStyle);
    XSSFCellUtil.setCellValue(cell, title);
  }

  private void writeListHeader() {
    XSSFRow row = worksheet.createRow(listStartRowIndex);
    for (SimpleListColumnModel<E, ?> column : columns) {
      XSSFCell cell = row.createCell(column.getIndex());
      cell.setCellStyle(listHeaderStyles.get(column));
      XSSFCellUtil.setCellValue(cell, column.getTitle());
    }
  }

  private void writeListData() {
    for (int i = 0; i < data.size(); i++) {
      E entity = data.get(i);
      XSSFRow row = worksheet.createRow(listStartRowIndex + 1 + i);
      for (SimpleListColumnModel<E, ?> column : columns) {
        XSSFCell cell = row.createCell(column.getIndex());
        cell.setCellStyle(listDataStyles.get(i % 2).get(column));
        XSSFCellUtil.setCellValue(cell, column.getPropertyGetter().apply(entity));
      }
    }
  }

  protected XSSFCellStyle createTitleStyle() {
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

  protected Map<SimpleListColumnModel<E, ?>, XSSFCellStyle> createListHeaderStyles() {
    XSSFFont standardFont = workbook.getStylesSource().getFontAt(0);

    XSSFFont font = workbook.createFont();
    font.setBold(true);
    font.setColor(standardFont.getColor());
    font.setFontName(standardFont.getFontName());
    font.setFontHeightInPoints(standardFont.getFontHeightInPoints());

    XSSFCellStyle style = workbook.createCellStyle();
    style.setFont(font);

    Map<SimpleListColumnModel<E, ?>, XSSFCellStyle> styles = new HashMap<>();
    columns.stream().forEach(column -> styles.put(column, style));
    return Collections.unmodifiableMap(styles);
  }

  protected List<Map<SimpleListColumnModel<E, ?>, XSSFCellStyle>> createListDataStyles() {
    final XSSFColor fillColor =
        new XSSFColor(FILL_COLOR_FOR_EVEN_ROW, workbook.getStylesSource().getIndexedColors());

    Map<SimpleListColumnModel<E, ?>, XSSFCellStyle> oddStyles = new HashMap<>();
    for (SimpleListColumnModel<E, ?> column : columns) {
      XSSFCellStyle style = workbook.createCellStyle();
      style.setFillPattern(FillPatternType.NO_FILL);
      style.setDataFormat(column.getFormatIndex(workbook));
      oddStyles.put(column, style);
    }

    Map<SimpleListColumnModel<E, ?>, XSSFCellStyle> evenStyles = new HashMap<>();
    for (SimpleListColumnModel<E, ?> column : columns) {
      XSSFCellStyle style = workbook.createCellStyle();
      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      style.setFillForegroundColor(fillColor);
      style.setDataFormat(column.getFormatIndex(workbook));
      evenStyles.put(column, style);
    }

    List<Map<SimpleListColumnModel<E, ?>, XSSFCellStyle>> styles = new ArrayList<>(2);
    styles.add(Collections.unmodifiableMap(oddStyles));
    styles.add(Collections.unmodifiableMap(evenStyles));
    return Collections.unmodifiableList(styles);
  }

  public SimpleListWorksheetModel addColumn(SimpleListColumnModel<E, ?> column) {
    columns.add(column);
    return this;
  }

  public String getSheetName() {
    return sheetName;
  }

  public String getTitle() {
    return title;
  }

  public List<E> getData() {
    return data;
  }

  public List<SimpleListColumnModel<E, ?>> getColumns() {
    return columns;
  }

  public int getTitleRowIndex() {
    return titleRowIndex;
  }

  public int getTitleColumnIndex() {
    return titleColumnIndex;
  }

  public int getListStartRowIndex() {
    return listStartRowIndex;
  }
}
