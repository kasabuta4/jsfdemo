package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Colors {
  public static XSSFColor create(XSSFWorkbook workbook, int red, int green, int blue) {
    XSSFColor color = workbook.getCreationHelper().createExtendedColor();
    color.setRGB(new byte[] {(byte) red, (byte) green, (byte) blue});
    return color;
  }
}
