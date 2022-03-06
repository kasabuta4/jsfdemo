package com.gmail.kasabuta4.jsfdemo.common.application;

import com.gmail.kasabuta4.jsfdemo.common.application.html.MultiColGroupsHtmlTable;
import com.gmail.kasabuta4.jsfdemo.common.jsf.message.FacesMessageProducer;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public abstract class MultiColGroupsTableView<P extends Serializable, E extends Serializable, R, C>
    implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final String MESSAGE_NOT_FOUND = "検索条件を満たすデータはありません";

  @NotNull @Valid private P condition;
  private MultiColGroupsHtmlTable<R, C, E> result;

  protected MultiColGroupsTableView(P condition) {
    this.condition = condition;
  }

  protected abstract MultiColGroupsTableFacade<P, E, R, C> getFacade();

  protected abstract Logger getLogger();

  protected abstract String getDestinationOnFound();

  protected String getDestinationOnNotFound() {
    return null;
  }

  protected abstract MultiColGroupsHtmlTable<R, C, E> createMultiColGroupsHtmlTable(
      Map<R, Map<C, E>> data);

  public String show() {
    try {
      Map<R, Map<C, E>> searchResult = getFacade().search(condition);
      if (searchResult.isEmpty() && getDestinationOnNotFound() == null) {
        FacesContext.getCurrentInstance()
            .addMessage(null, FacesMessageProducer.error(MESSAGE_NOT_FOUND));
        return null;
      }
      result = createMultiColGroupsHtmlTable(searchResult);
      return searchResult.isEmpty() ? getDestinationOnNotFound() : getDestinationOnFound();
    } catch (ApplicationException ex) {
      FacesContext.getCurrentInstance().addMessage(null, FacesMessageProducer.error(ex));
      return null;
    }
  }

  public P getCondition() {
    return condition;
  }

  public MultiColGroupsHtmlTable<R, C, E> getResult() {
    return result;
  }
}
