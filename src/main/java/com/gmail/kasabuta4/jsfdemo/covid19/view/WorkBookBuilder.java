package com.gmail.kasabuta4.jsfdemo.covid19.view;

import com.gmail.kasabuta4.jsfdemo.covid19.application.MonthlyNewCasesSummary;
import java.time.YearMonth;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class WorkBookBuilder {

  private final MonthlyNewCasesSummary summary;

  WorkBookBuilder(MonthlyNewCasesSummary summary) {
    this.summary = summary;
  }

  Workbook build() {
    XSSFWorkbook wb = new XSSFWorkbook();
    buildWorksheet(wb);
    return wb;
  }

  private void buildWorksheet(XSSFWorkbook wb) {
    XSSFSheet ws = wb.createSheet();
    writeTitle(ws);
    writeTableHeader(ws);
    writeTableBody(ws);
  }

  private void writeTitle(XSSFSheet ws) {
    XSSFRow titleRow = ws.createRow(0);
    titleRow.createCell(0).setCellValue("都道府県別月間新規感染者数");
  }

  private void writeTableHeader(XSSFSheet ws) {
    XSSFRow headerRow = ws.createRow(2);
    headerRow.createCell(0).setCellValue("都道府県");

    XSSFDataFormat yearMonthFormat = ws.getWorkbook().createDataFormat();
    XSSFCellStyle yearMonthStyle = ws.getWorkbook().createCellStyle();
    yearMonthStyle.setDataFormat(yearMonthFormat.getFormat("yyyy-MM"));
    for (int i = 0; i < summary.getYearMonths().size(); i++) {
      XSSFCell cell = headerRow.createCell(i + 1);
      cell.setCellValue(summary.getYearMonths().get(i).atDay(1));
      cell.setCellStyle(yearMonthStyle);
    }
  }

  private void writeTableBody(XSSFSheet ws) {
    XSSFDataFormat numberFormat = ws.getWorkbook().createDataFormat();
    XSSFCellStyle numberStyle = ws.getWorkbook().createCellStyle();
    numberStyle.setDataFormat(numberFormat.getFormat("#,##0"));

    for (int r = 0; r < summary.getPrefectures().size(); r++) {
      XSSFRow row = ws.createRow(r + 3);

      String prefecture = summary.getPrefectures().get(r);
      row.createCell(0).setCellValue(prefecture);

      for (int c = 0; c < summary.getYearMonths().size(); c++) {
        XSSFCell cell = row.createCell(c + 1);
        YearMonth yearMonth = summary.getYearMonths().get(c);
        cell.setCellValue(summary.getTable().get(prefecture).get(yearMonth));
        cell.setCellStyle(numberStyle);
      }
    }
  }
}
