package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WorkbookModel {

  // required properties
  private final String fileName;
  private final Function<XSSFWorkbook, Map<String, XSSFCellStyle>> styleMapProducer;
  private final List<AbstractTable> tables = new ArrayList<>();
  private final XSSFWorkbook workbook = new XSSFWorkbook();

  // optional properties
  private Consumer<XSSFCellStyle> standardStyleChanger = WorkbookModel::defaultStandardStyle;

  // temporary variables used during building workbook
  private Map<String, XSSFCellStyle> styleMap;

  public WorkbookModel(
      String fileName, Function<XSSFWorkbook, Map<String, XSSFCellStyle>> styleMapProducer) {
    this.fileName = fileName;
    this.styleMapProducer = styleMapProducer;
  }

  public WorkbookModel standardStyleChanger(Consumer<XSSFCellStyle> standardStyleChanger) {
    this.standardStyleChanger = standardStyleChanger;
    return this;
  }

  public <E> SimpleTable<E> addSimpleTable(String sheetName, List<E> data) {
    SimpleTable<E> table = new SimpleTable<>(this, sheetName, data);
    tables.add(table);
    return table;
  }

  public <R, C, E> MapTable<R, C, E> addMapTable(String sheetName, Map<R, Map<C, E>> data) {
    MapTable<R, C, E> table = new MapTable<>(this, sheetName, data);
    tables.add(table);
    return table;
  }

  public XSSFWorkbook build() {
    changeStandardStyle();
    initStyleMap();
    tables.stream().forEach(table -> table.build(workbook));
    return workbook;
  }

  public String getFileName() {
    return fileName;
  }

  XSSFCellStyle styleOf(String styleKey) {
    if (styleKey == null || styleMap == null || !styleMap.containsKey(styleKey))
      return workbook.getStylesSource().getStyleAt(0);
    return styleMap.get(styleKey);
  }

  private void changeStandardStyle() {
    if (standardStyleChanger != null)
      standardStyleChanger.accept(workbook.getStylesSource().getStyleAt(0));
  }

  private void initStyleMap() {
    if (styleMapProducer != null) styleMap = styleMapProducer.apply(workbook);
  }

  private static void defaultStandardStyle(XSSFCellStyle standardStyle) {
    XSSFFont standardFont = standardStyle.getFont();
    standardFont.setFontName("ヒラギノ明朝 ProN");
  }
}
