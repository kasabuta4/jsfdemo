package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import java.util.List;
import java.util.function.Function;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class KeyHeader<MapColumn, Y, P> {

  // required properties
  private final MapColumn mapColumn;
  private final Function<Y, P> propertyGetter;

  // optional properties
  private Function<P, ?> converter = Function.identity();
  private String styleKey;

  KeyHeader(MapColumn mapColumn, Function<Y, P> propertyGetter) {
    this.mapColumn = mapColumn;
    this.propertyGetter = propertyGetter;
  }

  public MapColumn endKeyHeader() {
    return mapColumn;
  }

  public KeyHeader<MapColumn, Y, P> converter(Function<P, ?> converter) {
    this.converter = converter;
    return this;
  }

  public KeyHeader<MapColumn, Y, P> styleKey(String styleKey) {
    this.styleKey = styleKey;
    return this;
  }

  void writeKeyHeader(List<Y> columnKeys, XSSFSheet sheet, int rowIndex, int columnIndex) {
    XSSFCellStyle style = getStyleKey() == null ? null : getWorkbookModel().styleOf(getStyleKey());
    for (int i = 0; i < columnKeys.size(); i++) {
      XSSFCell cell = sheet.getRow(rowIndex).createCell(columnIndex + i);
      if (style != null) cell.setCellStyle(style);
      Cells.setCellValue(cell, property(columnKeys.get(i)));
    }
  }

  private Object property(Y columnKey) {
    return propertyGetter.andThen(converter).apply(columnKey);
  }

  private WorkbookModel getWorkbookModel() {
    return ((AbstractColumn) mapColumn).getWorkbookModel();
  }

  private String getStyleKey() {
    return styleKey == null ? ((AbstractColumn) mapColumn).getHeaderStyleKey() : styleKey;
  }
}
