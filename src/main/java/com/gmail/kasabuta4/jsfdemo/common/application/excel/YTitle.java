package com.gmail.kasabuta4.jsfdemo.common.application.excel;

import java.util.function.Function;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class YTitle<X, Y, E, V, P> {

  // required properties
  private final YColumn<X, Y, E, V> yColumn;
  private final Function<Y, P> propertyGetter;
  private final NumberFormat format;

  // optional properties
  private Function<P, ?> converter = Function.identity();
  private Stylers.Formattable styler = Stylers.forFormattableTableHeader();

  // temporary variables used during building workbook
  private XSSFCellStyle style;

  YTitle(YColumn<X, Y, E, V> yColumn, Function<Y, P> propertyGetter, NumberFormat format) {
    this.yColumn = yColumn;
    this.propertyGetter = propertyGetter;
    this.format = format;
  }

  public YColumn<X, Y, E, V> endYTitle() {
    return yColumn;
  }

  public YTitle<X, Y, E, V, P> converter(Function<P, ?> converter) {
    this.converter = converter;
    return this;
  }

  public YTitle<X, Y, E, V, P> styler(Stylers.Formattable styler) {
    this.styler = styler;
    return this;
  }

  void initStyles(XSSFWorkbook workbook) {
    style = styler.createStyle(workbook, format);
  }

  void writeHeader(XSSFCell cell, Y y) {
    cell.setCellStyle(style);
    XSSFCellUtil.setCellValue(cell, propertyGetter.andThen(converter).apply(y));
  }
}
