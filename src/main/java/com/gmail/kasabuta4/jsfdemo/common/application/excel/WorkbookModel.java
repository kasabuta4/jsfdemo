package com.gmail.kasabuta4.jsfdemo.common.application.excel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WorkbookModel {

  // required properties
  private final String fileName;
  private final List<WorkSheetModel> worksheetModels = new ArrayList<>();
  private final XSSFWorkbook workbook = new XSSFWorkbook();

  public WorkbookModel(String fileName) {
    this.fileName = fileName;
  }

  public <E> SimpleListWorksheetModel<E> addWorksheetModel(
      String sheetName, String title, List<E> data) {
    SimpleListWorksheetModel<E> worksheetModel =
        new SimpleListWorksheetModel<>(this, sheetName, title, data);
    worksheetModels.add(worksheetModel);
    return worksheetModel;
  }

  public <R, C, E> XYTable<R, C, E> addMultiColGroupsTable(
      String sheetName, String title, Map<R, Map<C, E>> data) {
    XYTable<R, C, E> table = new XYTable<>(this, sheetName, title, data);
    worksheetModels.add(table);
    return table;
  }

  public XSSFWorkbook build() {
    worksheetModels.stream().forEach(WorkSheetModel::build);
    return workbook;
  }

  public String getFileName() {
    return fileName;
  }

  XSSFWorkbook getWorkbook() {
    return workbook;
  }
}
