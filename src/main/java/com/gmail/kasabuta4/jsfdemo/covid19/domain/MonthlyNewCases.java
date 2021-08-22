package com.gmail.kasabuta4.jsfdemo.covid19.domain;

import java.io.Serializable;
import java.time.YearMonth;

public class MonthlyNewCases implements Serializable {

  private static final long serialVersionUID = 1L;

  private YearMonth yearMonth;
  private String prefecture;
  private int cases;

  public MonthlyNewCases(YearMonth yearMonth, String prefecture, int cases) {
    this.yearMonth = yearMonth;
    this.prefecture = prefecture;
    this.cases = cases;
  }

  public YearMonth getYearMonth() {
    return yearMonth;
  }

  public void setYearMonth(YearMonth yearMonth) {
    this.yearMonth = yearMonth;
  }

  public String getPrefecture() {
    return prefecture;
  }

  public void setPrefecture(String prefecture) {
    this.prefecture = prefecture;
  }

  public int getCases() {
    return cases;
  }

  public void setCases(int cases) {
    this.cases = cases;
  }
}
