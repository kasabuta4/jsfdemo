package com.gmail.kasabuta4.jsfdemo.common.poi;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SimpleListColumnModel<E, X, Y> {

  private static final Color FILL_COLOR_FOR_EVEN_ROW = new Color(224, 255, 255);

  // required properties
  private final SimpleListWorksheetModel<E> worksheetModel;
  private final String title;
  protected final int characters;
  private final NumberFormat format;
  private final Function<X, Y> propertyGetter;

  // optional properties
  private Function<Y, ?> converter = Function.identity();
  private Function<XSSFWorkbook, XSSFCellStyle> titleStyleProducer = this::createTitleStyle;
  private Function<XSSFWorkbook, List<XSSFCellStyle>> dataStylesProducer = this::createDataStyles;

  // temporary variables used during building workbook
  private XSSFCellStyle titleStyle;
  private List<XSSFCellStyle> dataStyles;

  SimpleListColumnModel(
      SimpleListWorksheetModel<E> worksheetModel,
      String header,
      Function<X, Y> property,
      int characters,
      NumberFormat format) {
    this.worksheetModel = worksheetModel;
    this.title = header;
    this.propertyGetter = property;
    this.characters = characters;
    this.format = format;
  }

  public SimpleListWorksheetModel<E> endColumn() {
    return worksheetModel;
  }

  public SimpleListColumnModel<E, X, Y> converter(Function<Y, ?> converter) {
    this.converter = converter;
    return this;
  }

  public SimpleListColumnModel<E, X, Y> titleStyle(
      Function<XSSFWorkbook, XSSFCellStyle> titleStyleProducer) {
    this.titleStyleProducer = titleStyleProducer;
    return this;
  }

  public SimpleListColumnModel<E, X, Y> dataStyles(
      Function<XSSFWorkbook, List<XSSFCellStyle>> dataStylesProducer) {
    this.dataStylesProducer = dataStylesProducer;
    return this;
  }

  void initStyles(XSSFWorkbook workbook) {
    titleStyle = titleStyleProducer.apply(workbook);
    dataStyles = dataStylesProducer.apply(workbook);
  }

  void writeTitle(XSSFCell cell) {
    cell.setCellStyle(titleStyle);
    XSSFCellUtil.setCellValue(cell, title);
  }

  void writeData(X entity, XSSFCell cell, int rowIndex) {
    cell.setCellStyle(dataStyles.get(rowIndex % dataStyles.size()));
    XSSFCellUtil.setCellValue(cell, propertyGetter.andThen(converter).apply(entity));
  }

  private XSSFCellStyle createTitleStyle(XSSFWorkbook workbook) {
    XSSFFont standardFont = workbook.getStylesSource().getFontAt(0);

    XSSFFont font = workbook.createFont();
    font.setBold(true);
    font.setColor(standardFont.getColor());
    font.setFontName(standardFont.getFontName());
    font.setFontHeightInPoints(standardFont.getFontHeightInPoints());

    XSSFCellStyle style = workbook.createCellStyle();
    style.setFont(font);

    return style;
  }

  private List<XSSFCellStyle> createDataStyles(XSSFWorkbook workbook) {
    final XSSFColor fillColor =
        new XSSFColor(FILL_COLOR_FOR_EVEN_ROW, workbook.getStylesSource().getIndexedColors());

    XSSFCellStyle oddStyle = workbook.createCellStyle();
    oddStyle.setFillPattern(FillPatternType.NO_FILL);
    oddStyle.setDataFormat(getFormatIndex(workbook));

    XSSFCellStyle evenStyle = workbook.createCellStyle();
    evenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    evenStyle.setFillForegroundColor(fillColor);
    evenStyle.setDataFormat(getFormatIndex(workbook));

    List<XSSFCellStyle> styles = new ArrayList<>(2);
    styles.add(oddStyle);
    styles.add(evenStyle);
    return Collections.unmodifiableList(styles);
  }

  int getColumnWidth() {
    return (characters + 1) * 256;
  }

  private short getFormatIndex(XSSFWorkbook workbook) {
    return format.getFormat(workbook);
  }
}
