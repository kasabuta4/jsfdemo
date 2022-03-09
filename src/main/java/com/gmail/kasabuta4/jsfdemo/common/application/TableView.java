package com.gmail.kasabuta4.jsfdemo.common.application;

import com.gmail.kasabuta4.jsfdemo.common.application.excel.WorkbookModel;
import com.gmail.kasabuta4.jsfdemo.common.jsf.message.FacesMessageProducer;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public abstract class TableView<C, E, R, T> implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final String MESSAGE_NOT_FOUND = "検索条件を満たすデータはありません";

  private static final String CONTENT_TYPE_FOR_EXCEL_WORKBOOK =
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=utf-8";

  private static final String CONTENT_DISPOSITION_HEADER_NAME = "Content-Disposition";
  private static final String CONTENT_DISPOSITION_TEMPLATE_FOR_EXCEL_WORKBOOK =
      "attachment; filename=\"{0}\"";

  @NotNull @Valid private C condition;
  private T result;

  protected TableView(C condition) {
    this.condition = condition;
  }

  protected abstract SimpleSearchFacade<C, R, E> getFacade();

  protected abstract Logger getLogger();

  protected abstract String getDestinationOnFound();

  protected String getDestinationOnNotFound() {
    return null;
  }

  protected abstract boolean isEmpty(R searchResult);

  protected abstract T createHtmlTable(R result);

  protected abstract WorkbookModel createWorkbookModel(R result);

  public String show() {
    try {
      R searchResult = getFacade().search(condition);
      if (isEmpty(searchResult) && getDestinationOnNotFound() == null) {
        FacesContext.getCurrentInstance()
            .addMessage(null, FacesMessageProducer.error(MESSAGE_NOT_FOUND));
        return null;
      }
      result = createHtmlTable(searchResult);
      return isEmpty(searchResult) ? getDestinationOnNotFound() : getDestinationOnFound();
    } catch (ApplicationException ex) {
      FacesContext.getCurrentInstance().addMessage(null, FacesMessageProducer.error(ex));
      return null;
    }
  }

  public void outputExcel() {
    try {
      R searchResult = getFacade().search(condition);

      if (isEmpty(searchResult)) {
        FacesContext.getCurrentInstance()
            .addMessage(null, FacesMessageProducer.error(MESSAGE_NOT_FOUND));
        return;
      }

      WorkbookModel model = createWorkbookModel(searchResult);
      XSSFWorkbook workbook = model.build();

      String encodedFileName =
          URLEncoder.encode(model.getFileName(), StandardCharsets.UTF_8.name());
      FacesContext context = FacesContext.getCurrentInstance();
      HttpServletResponse res = (HttpServletResponse) context.getExternalContext().getResponse();
      res.setContentType(CONTENT_TYPE_FOR_EXCEL_WORKBOOK);
      res.setHeader(
          CONTENT_DISPOSITION_HEADER_NAME,
          MessageFormat.format(CONTENT_DISPOSITION_TEMPLATE_FOR_EXCEL_WORKBOOK, encodedFileName));

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

  public T getResult() {
    return result;
  }
}
