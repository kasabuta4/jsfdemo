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

  public <E> SimpleTable<E> addSimpleTable(String sheetName, String title, List<E> data) {
    SimpleTable<E> table = new SimpleTable<>(this, sheetName, title, data);
    worksheetModels.add(table);
    return table;
  }

  public <R, C, E> XYTable<R, C, E> addXYTable(
      String sheetName, String title, Map<R, Map<C, E>> data) {
    XYTable<R, C, E> table = new XYTable<>(this, sheetName, title, data);
    worksheetModels.add(table);
    return table;
  }

  public XSSFWorkbook build() {
    worksheetModels.stream().forEach(worksheetModel -> worksheetModel.build(workbook));
    return workbook;
  }

  public String getFileName() {
    return fileName;
  }
}
