package com.gmail.kasabuta4.jsfdemo.common.view.excel;

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

public class YColumn<X, Y, E, V> {

  // required properties
  private final XYTable<X, Y, E> table;
  private final String header;
  private final List<Y> yList;
  private final Function<E, V> propertyGetter;
  private final int characters;
  private final NumberFormat format;
  private final List<YTitle<X, Y, E, V, ?>> yTitles = new ArrayList<>();

  // optional properties
  private Function<V, ?> converter = Function.identity();
  private Stylers.Simple headerStyler = Stylers.forTableHeader();
  private Stylers.FormattableList bodyStyler = Stylers.forFormattableTableBody();

  // temporary variables used during building workbook
  private XSSFCellStyle headerStyle;
  private List<XSSFCellStyle> bodyStyles;

  YColumn(
      XYTable<X, Y, E> table,
      List<Y> yList,
      String header,
      Predicate<Y> yFilter,
      Function<E, V> propertyGetter,
      int characters,
      NumberFormat format) {
    this.table = table;
    this.header = header;
    this.yList = yList.stream().filter(yFilter).collect(toList());
    this.propertyGetter = propertyGetter;
    this.characters = characters;
    this.format = format;
  }

  public XYTable<X, Y, E> endYColumn() {
    return table;
  }

  public YColumn<X, Y, E, V> converter(Function<V, ?> converter) {
    this.converter = converter;
    return this;
  }

  public YColumn<X, Y, E, V> titleStyler(Stylers.Simple titleStyler) {
    this.headerStyler = titleStyler;
    return this;
  }

  public YColumn<X, Y, E, V> dataStyler(Stylers.FormattableList dataStyler) {
    this.bodyStyler = dataStyler;
    return this;
  }

  public YTitle<X, Y, E, V, Y> addIdentityYTitle(NumberFormat format) {
    YTitle<X, Y, E, V, Y> yTitle = new YTitle<>(this, Function.identity(), format);
    yTitles.add(yTitle);
    return yTitle;
  }

  void initStyles(XSSFWorkbook workbook) {
    headerStyle = headerStyler.createStyle(workbook);
    bodyStyles = bodyStyler.createStyles(workbook, format);
    yTitles.stream().forEach(yTitle -> yTitle.initStyles(workbook));
  }

  void writeHeader(XSSFSheet sheet, int rowIndex, int columnIndex, int maxColumnTitles) {
    if (yList.isEmpty()) return;

    XSSFRow headerRow = sheet.getRow(rowIndex);
    XSSFCell headerCell = headerRow.createCell(columnIndex);
    headerCell.setCellStyle(headerStyle);
    XSSFCellUtil.setCellValue(headerCell, header);
    if (yList.size() > 1)
      sheet.addMergedRegion(
          new CellRangeAddress(rowIndex, rowIndex, columnIndex, columnIndex + yList.size() - 1));

    for (int i = 0; i < yTitles.size(); i++) {
      YTitle<X, Y, E, V, ?> yTitle = yTitles.get(i);
      XSSFRow row =
          sheet.getRow(rowIndex + 1 + i) == null
              ? sheet.createRow(rowIndex + 1 + i)
              : sheet.getRow(rowIndex + 1 + i);
      for (int j = 0; j < yList.size(); j++) {
        XSSFCell cell = row.createCell(columnIndex + j);
        yTitle.writeHeader(cell, yList.get(j));
      }
    }
    if (yTitles.size() < maxColumnTitles)
      for (int j = 0; j < yList.size(); j++)
        sheet.addMergedRegion(
            new CellRangeAddress(
                rowIndex + yTitles.size(),
                rowIndex + maxColumnTitles,
                columnIndex + j,
                columnIndex + j));
  }

  void writeBody(Map<Y, E> map, XSSFRow row, int columnStartIndex, int rowIndex) {
    for (int i = 0; i < yList.size(); i++) {
      E entity = map.get(yList.get(i));
      XSSFCell cell = row.createCell(columnStartIndex + i);
      cell.setCellStyle(bodyStyles.get(rowIndex % bodyStyles.size()));
      XSSFCellUtil.setCellValue(cell, propertyGetter.andThen(converter).apply(entity));
    }
  }

  int getColumnWidth() {
    return (characters + 1) * 256;
  }

  List<Y> getYList() {
    return yList;
  }

  List<YTitle<X, Y, E, V, ?>> getYTitles() {
    return yTitles;
  }
}
