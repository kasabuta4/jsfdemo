package com.gmail.kasabuta4.jsfdemo.common.poi;

import java.util.Objects;
import java.util.function.Function;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SimpleListColumnModel<E, P> {

  private final SimpleListWorksheetModel<E> worksheet;
  private final int index;
  private final String title;
  private final int characters;
  private final NumberFormat format;
  private final Function<E, P> propertyGetter;

  protected SimpleListColumnModel(
      SimpleListWorksheetModel<E> worksheet,
      int columnIndex,
      String header,
      int characters,
      NumberFormat format,
      Function<E, P> property) {
    this.worksheet = worksheet;
    this.index = columnIndex;
    this.title = header;
    this.characters = characters;
    this.format = format;
    this.propertyGetter = property;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    if (!(o instanceof SimpleListColumnModel)) return false;
    final SimpleListColumnModel<?, ?> other = (SimpleListColumnModel<?, ?>) o;
    return Objects.equals(worksheet, other.worksheet) && Objects.equals(index, other.index);
  }

  @Override
  public int hashCode() {
    return Objects.hash(worksheet, index);
  }

  public int getIndex() {
    return index;
  }

  public String getTitle() {
    return title;
  }

  public int getColumnWidth() {
    return (characters + 1) * 256;
  }

  public short getFormatIndex(XSSFWorkbook wb) {
    return format.getFormat(wb);
  }

  public Function<E, P> getPropertyGetter() {
    return propertyGetter;
  }
}
