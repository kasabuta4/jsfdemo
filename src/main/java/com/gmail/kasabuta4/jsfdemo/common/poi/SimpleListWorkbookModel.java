package com.gmail.kasabuta4.jsfdemo.common.poi;

import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SimpleListWorkbookModel {

  private final String fileName;
  private final List<SimpleListWorksheetModel<?>> worksheetModels;

  private final XSSFWorkbook workbook;

  public SimpleListWorkbookModel(String fileName) {
    this.fileName = fileName;
    this.worksheetModels = new ArrayList<>();
    this.workbook = new XSSFWorkbook();
  }

  public <E> SimpleListWorksheetModel<E> addWorksheetModel(
      String sheetName, String title, List<E> data) {
    SimpleListWorksheetModel<E> worksheetModel =
        new SimpleListWorksheetModel<>(workbook, sheetName, title, data);
    worksheetModels.add(worksheetModel);
    return worksheetModel;
  }

  public XSSFWorkbook build() {
    worksheetModels.stream().forEach(SimpleListWorksheetModel::build);
    return workbook;
  }

  public String getFileName() {
    return fileName;
  }
}
