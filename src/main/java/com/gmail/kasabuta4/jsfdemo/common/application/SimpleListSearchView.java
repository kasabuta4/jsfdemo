package com.gmail.kasabuta4.jsfdemo.common.application;

import com.gmail.kasabuta4.jsfdemo.common.application.excel.SimpleListWorkbookModel;
import com.gmail.kasabuta4.jsfdemo.common.application.html.SimpleListHtmlTableModel;
import com.gmail.kasabuta4.jsfdemo.common.jsf.message.FacesMessageProducer;
import java.io.IOException;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class SimpleListSearchView<C extends Serializable, R extends Serializable>
    implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final String MESSAGE_NOT_FOUND = "検索条件を満たすデータはありません";

  private static final String CONTENT_TYPE_FOR_EXCEL_WORKBOOK =
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=utf-8";

  private static final String CONTENT_DISPOSITION_HEADER_NAME = "Content-Disposition";
  private static final String CONTENT_DISPOSITION_TEMPLATE_FOR_EXCEL_WORKBOOK =
      "attachment; filename=\"{0}\"";

  @NotNull @Valid private C condition;
  private SimpleListHtmlTableModel<R> result;

  protected SimpleListSearchView(C condition) {
    this.condition = condition;
  }

  protected abstract SimpleListSearchFacade<C, R> getFacade();

  protected abstract Logger getLogger();

  protected abstract String getDestinationOnFound();

  protected String getDestinationOnNotFound() {
    return null;
  }

  protected abstract SimpleListHtmlTableModel<R> createHtmlTableModel(List<R> result);

  protected abstract SimpleListWorkbookModel createWorkbookModel(List<R> result);

  public String show() {
    try {
      List<R> searchResult = getFacade().search(condition);
      if (searchResult.isEmpty() && getDestinationOnNotFound() == null) {
        FacesContext.getCurrentInstance()
            .addMessage(null, FacesMessageProducer.error(MESSAGE_NOT_FOUND));
        return null;
      }
      result = createHtmlTableModel(getFacade().search(condition));
      return result.getData().isEmpty() ? getDestinationOnNotFound() : getDestinationOnFound();
    } catch (ApplicationException ex) {
      FacesContext.getCurrentInstance().addMessage(null, FacesMessageProducer.error(ex));
      return null;
    }
  }

  public void outputExcel() {
    try {
      List<R> result = getFacade().search(condition);

      if (result.isEmpty()) {
        FacesContext.getCurrentInstance()
            .addMessage(null, FacesMessageProducer.error(MESSAGE_NOT_FOUND));
        return;
      }

      SimpleListWorkbookModel model = createWorkbookModel(result);
      XSSFWorkbook workbook = model.build();

      FacesContext context = FacesContext.getCurrentInstance();
      HttpServletResponse res = (HttpServletResponse) context.getExternalContext().getResponse();
      res.setContentType(CONTENT_TYPE_FOR_EXCEL_WORKBOOK);
      res.setHeader(
          CONTENT_DISPOSITION_HEADER_NAME,
          MessageFormat.format(
              CONTENT_DISPOSITION_TEMPLATE_FOR_EXCEL_WORKBOOK, model.getFileName()));
      workbook.write(res.getOutputStream());
      context.responseComplete();
    } catch (ApplicationException ex) {
      FacesContext.getCurrentInstance().addMessage(null, FacesMessageProducer.error(ex));
    } catch (IOException ex) {
      getLogger().log(Level.SEVERE, ex.getMessage(), ex);
    }
  }

  public C getCondition() {
    return condition;
  }

  public SimpleListHtmlTableModel<R> getResult() {
    return result;
  }
}
