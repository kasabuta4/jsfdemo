package com.gmail.kasabuta4.jsfdemo.covid19.view;

import java.io.Serializable;
import java.time.YearMonth;
import javax.validation.constraints.NotNull;

public class SearchCondition implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull(message = "表示期間の開始年月の指定は必須です")
  private YearMonth from = YearMonth.now().minusYears(1);

  @NotNull(message = "表示期間の終了年月の指定は必須です")
  private YearMonth to = YearMonth.now().minusMonths(1);

  public SearchCondition() {}

  public YearMonth getFrom() {
    return from;
  }

  public void setFrom(YearMonth from) {
    this.from = from;
  }

  public YearMonth getTo() {
    return to;
  }

  public void setTo(YearMonth to) {
    this.to = to;
  }
}
