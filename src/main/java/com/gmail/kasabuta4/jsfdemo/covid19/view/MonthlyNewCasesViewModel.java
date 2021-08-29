package com.gmail.kasabuta4.jsfdemo.covid19.view;

import static com.gmail.kasabuta4.jsfdemo.common.jsf.navigation.RedirectUtil.redirectTo;

import com.gmail.kasabuta4.jsfdemo.covid19.application.MonthlyNewCasesService;
import com.gmail.kasabuta4.jsfdemo.covid19.application.MonthlyNewCasesSummary;
import java.io.Serializable;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import org.primefaces.model.charts.line.LineChartModel;

@Named("monthlyNewCases")
@ConversationScoped
public class MonthlyNewCasesViewModel implements Serializable {

  private static final long serialVersionUID = 1L;

  @Inject transient Conversation conversation;

  @Inject transient MonthlyNewCasesService service;

  @Valid private SearchCondition searchCondition = new SearchCondition();

  private MonthlyNewCasesSummary summary;

  private LineChartModel lineChartModel;

  public String execute() {
    if (conversation.isTransient()) conversation.begin();
    doExecute();
    return redirectTo("result");
  }

  public String showLineChart() {
    if (conversation.isTransient()) conversation.begin();
    doShowLineChart();
    return redirectTo("result");
  }

  public String exit() {
    if (!conversation.isTransient()) conversation.end();
    return redirectTo("/index");
  }

  private void doExecute() {
    summary = service.search(searchCondition.getFrom(), searchCondition.getTo());
  }

  private void doShowLineChart() {
    summary = service.search(searchCondition.getFrom(), searchCondition.getTo());
    lineChartModel = new LineChartModelBuilder(summary).build();
  }

  public SearchCondition getSearchCondition() {
    return searchCondition;
  }

  public MonthlyNewCasesSummary getSummary() {
    return summary;
  }

  public LineChartModel getLineChartModel() {
    return lineChartModel;
  }
}
