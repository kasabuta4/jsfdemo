package com.gmail.kasabuta4.jsfdemo.covid19.application;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.util.stream.Collectors.toList;

import com.gmail.kasabuta4.jsfdemo.common.application.SimpleSearchFacade;
import com.gmail.kasabuta4.jsfdemo.covid19.domain.MonthlyNewCases;
import com.gmail.kasabuta4.jsfdemo.covid19.domain.SearchCondition;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Stream;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;

@Dependent
public class MonthlyNewCasesSimpleTableFacade
    extends SimpleSearchFacade<SearchCondition, List<MonthlyNewCases>, MonthlyNewCases> {

  private static final long serialVersionUID = 1L;

  private static final String BY_PREFECTURE_SQL =
      "SELECT "
          + "    YEAR(REPORTED_DATE) AS Y, "
          + "    MONTH(REPORTED_DATE) AS M, "
          + "    PREFECTURE, "
          + "    SUM(CASES) "
          + "FROM "
          + "    NEWLY_CONFIRMED_CASES_DAILY "
          + "WHERE "
          + "        PREFECTURE <> 'ALL' "
          + "    AND REPORTED_DATE >= ? "
          + "    AND REPORTED_DATE < ? "
          + "GROUP BY "
          + "    YEAR(REPORTED_DATE), "
          + "    MONTH(REPORTED_DATE), "
          + "    PREFECTURE "
          + " ORDER BY "
          + "    Y, "
          + "    M, "
          + "    PREFECTURE";

  @Override
  protected Stream<MonthlyNewCases> doSearch(EntityManager em, SearchCondition condition) {
    Stream rawStream =
        em.createNativeQuery(BY_PREFECTURE_SQL)
            .setParameter(1, condition.getFrom().atDay(1).format(ISO_LOCAL_DATE))
            .setParameter(2, condition.getTo().plusMonths(1).atDay(1).format(ISO_LOCAL_DATE))
            .getResultStream();
    Stream<Object[]> stream = (Stream<Object[]>) rawStream;
    return stream.map(this::toMonthlyNewCases);
  }

  @Override
  protected List<MonthlyNewCases> convert(Stream<MonthlyNewCases> stream) {
    return stream.collect(toList());
  }

  private MonthlyNewCases toMonthlyNewCases(Object[] a) {
    return new MonthlyNewCases(YearMonth.of((int) a[0], (int) a[1]), (String) a[2], (int) a[3]);
  }
}
