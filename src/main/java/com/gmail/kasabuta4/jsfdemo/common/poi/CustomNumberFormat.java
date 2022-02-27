package com.gmail.kasabuta4.jsfdemo.common.poi;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CustomNumberFormat implements NumberFormat {
  private String format;

  public CustomNumberFormat(String formant) {
    this.format = format;
  }

  @Override
  public short getFormat(XSSFWorkbook wb) {
    return wb.createDataFormat().getFormat(format);
  }
}
