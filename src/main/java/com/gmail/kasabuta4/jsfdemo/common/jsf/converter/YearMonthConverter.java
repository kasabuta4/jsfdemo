package com.gmail.kasabuta4.jsfdemo.common.jsf.converter;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

@FacesConverter(forClass = YearMonth.class, managed = true)
public class YearMonthConverter implements Converter<YearMonth> {

  private static final DateTimeFormatter YEAR_MONTH_FORMATTER =
      DateTimeFormatter.ofPattern("uuuu-MM");

  public YearMonthConverter() {}

  @Override
  public YearMonth getAsObject(FacesContext context, UIComponent component, String value) {
    if (value == null) return null;

    try {
      return YearMonth.parse(value, YEAR_MONTH_FORMATTER);
    } catch (DateTimeParseException ex) {
      throw new ConverterException(ex);
    }
  }

  @Override
  public String getAsString(FacesContext context, UIComponent component, YearMonth value) {
    if (value == null) return "";

    return value.format(YEAR_MONTH_FORMATTER);
  }
}
