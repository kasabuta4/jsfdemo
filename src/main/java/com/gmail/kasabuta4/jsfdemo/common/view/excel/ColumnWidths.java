package com.gmail.kasabuta4.jsfdemo.common.view.excel;

import java.util.function.Function;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ColumnWidths {

  public static ColumnWidth AUTO_SIZE =
      (XSSFSheet worksheet, int columnIndex) -> {
        worksheet.autoSizeColumn(columnIndex);
      };

  private static final Function<Integer, Integer> DEFAULT_CHARACTERS_TO_WIDTH = n -> (n + 1) * 256;

  public static ColumnWidth byWidth(int width) {
    return (XSSFSheet worksheet, int columnIndex) -> {
      worksheet.setColumnWidth(columnIndex, width);
    };
  }

  public static ColumnWidth byCharacters(int characters) {
    return byCharacters(characters, DEFAULT_CHARACTERS_TO_WIDTH);
  }

  public static ColumnWidth byCharacters(
      int characters, Function<Integer, Integer> charactersToWidth) {
    return (XSSFSheet worksheet, int columnIndex) -> {
      worksheet.setColumnWidth(columnIndex, charactersToWidth.apply(characters));
    };
  }
}
