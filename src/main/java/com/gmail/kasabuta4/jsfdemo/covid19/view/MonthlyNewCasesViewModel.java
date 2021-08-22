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

@Named("monthlyNewCases")
@ConversationScoped
public class MonthlyNewCasesViewModel implements Serializable {

  private static final long serialVersionUID = 1L;

  @Inject Conversation conversation;

  @Inject MonthlyNewCasesService service;

  @Valid private SearchCondition searchCondition = new SearchCondition();

  private MonthlyNewCasesSummary summary;

  public String execute() {
    if (conversation.isTransient()) conversation.begin();
    doExecute();
    return redirectTo("result");
  }

  public String exit() {
    if (!conversation.isTransient()) conversation.end();
    return redirectTo("/index");
  }

  private void doExecute() {
    summary = service.search(searchCondition.getFrom(), searchCondition.getTo());
  }

  public SearchCondition getSearchCondition() {
    return searchCondition;
  }

  public MonthlyNewCasesSummary getSummary() {
    return summary;
  }
}
