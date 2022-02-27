package com.gmail.kasabuta4.jsfdemo.covid19.view;

import com.gmail.kasabuta4.jsfdemo.common.application.SimpleListSearchFacade;
import com.gmail.kasabuta4.jsfdemo.common.application.SimpleListSearchView;
import com.gmail.kasabuta4.jsfdemo.common.poi.CommonNumberFormat;
import com.gmail.kasabuta4.jsfdemo.common.poi.SimpleListWorkbookModel;
import com.gmail.kasabuta4.jsfdemo.covid19.application.MonthlyNewCasesFacade;
import com.gmail.kasabuta4.jsfdemo.covid19.domain.MonthlyNewCases;
import com.gmail.kasabuta4.jsfdemo.covid19.domain.SearchCondition;
import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class MonthlyNewCasesView extends SimpleListSearchView<SearchCondition, MonthlyNewCases> {

  @Inject MonthlyNewCasesFacade facade;
  @Inject Logger logger;

  public MonthlyNewCasesView() {
    super(new SearchCondition());
  }

  @Override
  protected SimpleListSearchFacade<SearchCondition, MonthlyNewCases> getFacade() {
    return facade;
  }

  @Override
  protected Logger getLogger() {
    return logger;
  }

  @Override
  protected String getDestinationOnSuccess() {
    return "result.xhtml";
  }

  @Override
  protected SimpleListWorkbookModel createWorkbookModel(List<MonthlyNewCases> result) {
    SimpleListWorkbookModel model = new SimpleListWorkbookModel("MonthlyNewCases.xlsx");
    model
        .addWorksheetModel("list", "Covid-19", result)
        .addYearMonthColumn(0, "年月", MonthlyNewCases::getYearMonth)
        .addStringColumn(1, "都道府県", 20, MonthlyNewCases::getPrefecture)
        .addIntegerColumn(2, "新規感染者数", 12, CommonNumberFormat.桁区切り整数, MonthlyNewCases::getCases);
    return model;
  }
}
