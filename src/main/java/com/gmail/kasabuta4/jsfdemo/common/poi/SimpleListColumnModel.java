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
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SimpleListColumnModel<E, P> {

  private static final Color FILL_COLOR_FOR_EVEN_ROW = new Color(224, 255, 255);

  // required property
  private final SimpleListWorksheetModel<E> worksheet;
  private final int index;
  private final String title;
  protected final int characters;
  private final NumberFormat format;
  private final Function<E, P> propertyGetter;

  // temporary variables used during building workbook
  private XSSFCellStyle titleStyle;
  private List<XSSFCellStyle> dataStyles;

  protected SimpleListColumnModel(
      SimpleListWorksheetModel<E> worksheet,
      int columnIndex,
      String header,
      int characters,
      NumberFormat format,
      Function<E, P> property) {
    this.worksheet = worksheet;
    this.index = columnIndex;
    this.title = header;
    this.characters = characters;
    this.format = format;
    this.propertyGetter = property;
  }

  void initStyles(XSSFWorkbook workbook) {
    titleStyle = createTitleStyle(workbook);
    dataStyles = createDataStyles(workbook);
  }

  void setColumnWidth(XSSFSheet worksheet) {
    worksheet.setColumnWidth(index, getColumnWidth());
  }

  void writeTitle(XSSFRow row) {
    XSSFCell cell = row.createCell(index);
    cell.setCellStyle(titleStyle);
    XSSFCellUtil.setCellValue(cell, title);
  }

  void writeData(int rowIndex, E entity, XSSFRow row) {
    XSSFCell cell = row.createCell(index);
    cell.setCellStyle(dataStyles.get(rowIndex % dataStyles.size()));
    XSSFCellUtil.setCellValue(cell, propertyGetter.apply(entity));
  }

  protected XSSFCellStyle createTitleStyle(XSSFWorkbook workbook) {
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

  protected List<XSSFCellStyle> createDataStyles(XSSFWorkbook workbook) {
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

  protected int getColumnWidth() {
    return (characters + 1) * 256;
  }

  private short getFormatIndex(XSSFWorkbook workbook) {
    return format.getFormat(workbook);
  }
}
