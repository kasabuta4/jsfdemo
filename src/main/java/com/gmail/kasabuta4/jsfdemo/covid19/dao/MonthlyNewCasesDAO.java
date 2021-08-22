package com.gmail.kasabuta4.jsfdemo.covid19.dao;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.util.stream.Collectors.toList;

import com.gmail.kasabuta4.jsfdemo.config.db.JsfDemoDB;
import com.gmail.kasabuta4.jsfdemo.covid19.domain.MonthlyNewCases;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Stream;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class MonthlyNewCasesDAO {

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

  @Inject @JsfDemoDB EntityManager em;

  public List<MonthlyNewCases> getCases(YearMonth from, YearMonth to) {

    Stream<Object[]> stream =
        (Stream<Object[]>)
            em.createNativeQuery(BY_PREFECTURE_SQL)
                .setParameter(1, from.atDay(1).format(ISO_LOCAL_DATE))
                .setParameter(2, to.plusMonths(1).atDay(1).format(ISO_LOCAL_DATE))
                .getResultStream();

    return stream.map(this::toMonthlyNewCases).collect(toList());
  }

  private MonthlyNewCases toMonthlyNewCases(Object[] a) {
    return new MonthlyNewCases(YearMonth.of((int) a[0], (int) a[1]), (String) a[2], (int) a[3]);
  }
}
