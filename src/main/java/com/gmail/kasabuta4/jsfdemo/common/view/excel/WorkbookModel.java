package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WorkbookModel {

  // required properties
  private final String fileName;
  private final List<WorkSheetModel> worksheetModels = new ArrayList<>();
  private final XSSFWorkbook workbook = new XSSFWorkbook();

  // optional properties
  private Consumer<XSSFCellStyle> standardStyleChanger = WorkbookModel::changeStandardStyle;

  public WorkbookModel(String fileName) {
    this.fileName = fileName;
  }

  public WorkbookModel standardStyleChanger(Consumer<XSSFCellStyle> standardStyleChanger) {
    this.standardStyleChanger = standardStyleChanger;
    return this;
  }

  public <E> SimpleTable<E> addSimpleTable(String sheetName, String title, List<E> data) {
    SimpleTable<E> table = new SimpleTable<>(this, sheetName, title, data);
    worksheetModels.add(table);
    return table;
  }

  public <R, C, E> MapTable<R, C, E> addMapTable(
      String sheetName, String title, Map<R, Map<C, E>> data) {
    MapTable<R, C, E> table = new MapTable<>(this, sheetName, title, data);
    worksheetModels.add(table);
    return table;
  }

  public XSSFWorkbook build() {
    standardStyleChanger.accept(workbook.getStylesSource().getStyleAt(0));
    worksheetModels.stream().forEach(worksheetModel -> worksheetModel.build(workbook));
    return workbook;
  }

  public String getFileName() {
    return fileName;
  }

  private static void changeStandardStyle(XSSFCellStyle standardStyle) {
    XSSFFont standardFont = standardStyle.getFont();
    standardFont.setFontName("ヒラギノ明朝 ProN");
  }
}
