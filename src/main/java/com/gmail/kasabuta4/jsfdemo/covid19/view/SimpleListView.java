package com.gmail.kasabuta4.jsfdemo.covid19.view;

import com.gmail.kasabuta4.jsfdemo.common.poi.CommonNumberFormat;
import com.gmail.kasabuta4.jsfdemo.common.poi.SimpleListWorkbookModel;
import com.gmail.kasabuta4.jsfdemo.covid19.application.MonthlyNewCasesService;
import com.gmail.kasabuta4.jsfdemo.covid19.domain.MonthlyNewCases;
import java.io.IOException;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Named
@RequestScoped
public class SimpleListView {

  @Inject transient MonthlyNewCasesService service;

  @Valid private SearchCondition searchCondition = new SearchCondition();

  private List<MonthlyNewCases> list;

  public void execute() {
    list = service.searchInSimpleList(searchCondition.getFrom(), searchCondition.getTo());

    SimpleListWorkbookModel model = new SimpleListWorkbookModel("MonthlyNewCases.xlsx");
    model
        .addWorksheetModel("list", "Covid-19", list)
        .addYearMonthColumn(0, "年月", MonthlyNewCases::getYearMonth)
        .addStringColumn(1, "都道府県", 20, MonthlyNewCases::getPrefecture)
        .addIntegerColumn(2, "新規感染者数", 12, CommonNumberFormat.桁区切り整数, MonthlyNewCases::getCases);
    XSSFWorkbook workbook = model.build();

    FacesContext context = FacesContext.getCurrentInstance();
    HttpServletResponse res = (HttpServletResponse) context.getExternalContext().getResponse();
    res.setContentType(
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=utf-8");
    res.setHeader("Content-Disposition", "attachment; filename=\"MonthlyNewCases.xlsx\"");
    try {
      workbook.write(res.getOutputStream());
    } catch (IOException ex) {
      // TODO
    }
    FacesContext.getCurrentInstance().responseComplete();
  }

  public SearchCondition getSearchCondition() {
    return searchCondition;
  }
}
