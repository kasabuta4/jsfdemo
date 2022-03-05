package com.gmail.kasabuta4.jsfdemo.common.application.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public interface NumberFormat {
  short getFormat(XSSFWorkbook wb);
}
