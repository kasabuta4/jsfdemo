package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

interface WorkSheetModel {
  XSSFSheet build(XSSFWorkbook workbook);
}