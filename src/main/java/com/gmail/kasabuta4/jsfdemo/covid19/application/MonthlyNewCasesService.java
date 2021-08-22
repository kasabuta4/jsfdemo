package com.gmail.kasabuta4.jsfdemo.covid19.application;

import com.gmail.kasabuta4.jsfdemo.covid19.dao.MonthlyNewCasesDAO;
import java.time.YearMonth;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class MonthlyNewCasesService {

  @Inject private MonthlyNewCasesDAO dao;

  @Transactional
  public MonthlyNewCasesSummary search(YearMonth from, YearMonth to) {
    return new MonthlyNewCasesSummary(dao.getCases(from, to));
  }
}
