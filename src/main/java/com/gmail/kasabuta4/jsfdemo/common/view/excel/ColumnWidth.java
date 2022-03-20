package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import org.apache.poi.xssf.usermodel.XSSFSheet;

public interface ColumnWidth {

  void configure(XSSFSheet worksheet, int columnIndex);
}
