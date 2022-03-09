package com.gmail.kasabuta4.jsfdemo.common.view.html;

import java.text.DecimalFormat;
import java.text.Format;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class HtmlConverters {

  public static <T> Function<T, String> appendPrefix(String prefix) {
    return t -> prefix + t.toString();
  }

  public static <T> Function<T, String> appendSuffix(String suffix) {
    return t -> t.toString() + suffix;
  }

  public static Function<Integer, String> 桁区切り整数() {
    return fromInteger("#,##0");
  }

  public static Function<Integer, String> fromInteger(String pattern) {
    final Format formatter = new DecimalFormat(pattern);
    return n -> n == null ? null : formatter.format(n);
  }

  public static Function<YearMonth, String> 年月() {
    return fromYearMonth("uuuu/MM");
  }

  public static Function<YearMonth, String> fromYearMonth(String pattern) {
    return yearMonth ->
        yearMonth == null ? null : DateTimeFormatter.ofPattern(pattern).format(yearMonth);
  }
}
