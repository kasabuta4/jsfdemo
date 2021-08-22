package com.gmail.kasabuta4.jsfdemo.covid19.application;

import static java.util.stream.Collectors.toList;

import com.gmail.kasabuta4.jsfdemo.covid19.domain.MonthlyNewCases;
import java.io.Serializable;
import java.time.Period;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class MonthlyNewCasesSummary implements Serializable {

  private static final long serialVersionUID = 1L;

  private List<String> prefectures;
  private List<YearMonth> yearMonths;
  private Map<String, Map<YearMonth, Integer>> table;

  public MonthlyNewCasesSummary() {}

  public MonthlyNewCasesSummary(List<MonthlyNewCases> cases) {
    prefectures = distinctPrefectures(cases);
    yearMonths = distinctYearMonths(cases);
    table = toTable(cases);
  }

  public List<String> getPrefectures() {
    return prefectures;
  }

  public List<YearMonth> getYearMonths() {
    return yearMonths;
  }

  public Map<String, Map<YearMonth, Integer>> getTable() {
    return table;
  }

  private List<String> distinctPrefectures(List<MonthlyNewCases> cases) {
    return cases.stream().map(MonthlyNewCases::getPrefecture).distinct().collect(toList());
  }

  private List<YearMonth> distinctYearMonths(List<MonthlyNewCases> cases) {
    if (cases.isEmpty()) return Collections.emptyList();

    YearMonth oldest =
        cases.stream().map(MonthlyNewCases::getYearMonth).min(YearMonth::compareTo).get();

    YearMonth latest =
        cases.stream().map(MonthlyNewCases::getYearMonth).max(YearMonth::compareTo).get();

    return Stream.iterate(oldest, ym -> ym.plus(Period.ofMonths(1)))
        .limit(ChronoUnit.MONTHS.between(oldest, latest) + 1)
        .collect(toList());
  }

  private Map<String, Map<YearMonth, Integer>> toTable(List<MonthlyNewCases> cases) {
    final Map<String, Map<YearMonth, Integer>> map = new HashMap<>();

    for (String prefecture : prefectures) {
      Map<YearMonth, Integer> yearMonthMap = new HashMap<>();
      for (YearMonth yearMonth : yearMonths) {
        yearMonthMap.put(yearMonth, 0);
      }
      map.put(prefecture, yearMonthMap);
    }

    cases
        .stream()
        .forEach(
            c -> {
              map.get(c.getPrefecture()).put(c.getYearMonth(), c.getCases());
            });
    return Collections.unmodifiableMap(map);
  }
}
