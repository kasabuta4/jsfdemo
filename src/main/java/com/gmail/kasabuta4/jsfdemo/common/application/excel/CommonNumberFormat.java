package com.gmail.kasabuta4.jsfdemo.common.application.excel;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public enum CommonNumberFormat implements NumberFormat {
  標準("General"),
  整数("0"),
  桁区切り整数("#,##0"),
  年月("yyyy/mm");

  private final String format;

  @Override
  public short getFormat(XSSFWorkbook wb) {
    return wb.createDataFormat().getFormat(format);
  }

  CommonNumberFormat(String format) {
    this.format = format;
  }
}
