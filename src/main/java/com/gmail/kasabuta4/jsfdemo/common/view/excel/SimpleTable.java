package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import static com.gmail.kasabuta4.jsfdemo.common.view.excel.ColumnWidthConfigurators.byCharacters;
import static com.gmail.kasabuta4.jsfdemo.common.view.excel.CommonNumberFormat.年月;
import static com.gmail.kasabuta4.jsfdemo.common.view.excel.CommonNumberFormat.標準;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SimpleTable<E> extends AbstractTable<SimpleTable<E>> {

  // required properties
  private final List<E> data;
  private final List<SimpleColumn<SimpleTable<E>, E, ?>> columns = new ArrayList<>();

  protected SimpleTable(
      WorkbookModel workbookModel, String sheetName, String caption, List<E> data) {
    super(workbookModel, sheetName, caption);
    this.data = data;
  }

  @Override
  protected SimpleTable<E> self() {
    return this;
  }

  public SimpleColumn<SimpleTable<E>, E, String> addStringColumn(
      String header,
      Function<E, String> property,
      ColumnWidthConfigurator columnWidthConfigurator) {
    SimpleColumn<SimpleTable<E>, E, String> columnModel =
        new SimpleColumn<>(this, header, property, columnWidthConfigurator, 標準);
    columns.add(columnModel);
    return columnModel;
  }

  public SimpleColumn<SimpleTable<E>, E, Integer> addIntegerColumn(
      String header,
      Function<E, Integer> property,
      ColumnWidthConfigurator columnWidthConfigurator,
      NumberFormat format) {
    SimpleColumn<SimpleTable<E>, E, Integer> columnModel =
        new SimpleColumn<>(this, header, property, columnWidthConfigurator, format);
    columns.add(columnModel);
    return columnModel;
  }

  public SimpleColumn<SimpleTable<E>, E, Double> addDoubleColumn(
      String header,
      Function<E, Double> property,
      ColumnWidthConfigurator columnWidthConfigurator,
      NumberFormat format) {
    SimpleColumn<SimpleTable<E>, E, Double> columnModel =
        new SimpleColumn<>(this, header, property, columnWidthConfigurator, format);
    columns.add(columnModel);
    return columnModel;
  }

  public SimpleColumn<SimpleTable<E>, E, YearMonth> addYearMonthColumn(
      String header, Function<E, YearMonth> property) {
    SimpleColumn<SimpleTable<E>, E, YearMonth> columnModel =
        new SimpleColumn<>(this, header, property, byCharacters(7), 年月);
    columns.add(columnModel);
    return columnModel;
  }

  @Override
  protected void initStyles(XSSFWorkbook workbook) {
    super.initStyles(workbook);
    initColumnsStyles(workbook);
  }

  private void initColumnsStyles(XSSFWorkbook workbook) {
    for (SimpleColumn<SimpleTable<E>, E, ?> column : columns) column.initStyles(workbook);
  }

  @Override
  protected void writeHeader() {
    super.writeHeader();
    writeColumnsHeader();
  }

  private void writeColumnsHeader() {
    for (int i = 0; i < columns.size(); i++)
      columns.get(i).writeHeader(worksheet, headerStartRowIndex, columnIndex(i), 0);
  }

  @Override
  protected void writeBody() {
    for (int dataIndex = 0; dataIndex < data.size(); dataIndex++) writeRecord(dataIndex);
  }

  @Override
  protected void writeRecord(int dataIndex) {
    super.writeRecord(dataIndex);
    writeColumnsToRecord(dataIndex);
  }

  private void writeColumnsToRecord(int dataIndex) {
    for (int i = 0; i < columns.size(); i++)
      columns
          .get(i)
          .writeBodyRecord(
              data.get(dataIndex), dataIndex, worksheet, toRowIndex(dataIndex), columnIndex(i));
  }

  @Override
  protected void configureColumnWidth() {
    super.configureColumnWidth();
    configureColumnsWidth();
  }

  private void configureColumnsWidth() {
    for (int i = 0; i < columns.size(); i++)
      columns.get(i).configureColumnWidth(worksheet, columnIndex(i));
  }
}
