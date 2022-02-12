package com.gmail.kasabuta4.jsfdemo.user.view;

import com.gmail.kasabuta4.jsfdemo.user.entity.UserException;
import com.gmail.kasabuta4.jsfdemo.user.facade.UserManagementFacade;
import com.gmail.kasabuta4.jsfdemo.user.facade.ChangePasswordModel;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class ChangePasswordView {

  @Inject UserManagementFacade facade;

  private ChangePasswordModel model = new ChangePasswordModel();
  private boolean done;

  public void changePassword() {
    model.setName(
        FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName());
    try {
      facade.changePassword(model);
      done = true;
    } catch (UserException ex) {
      FacesContext.getCurrentInstance().addMessage(null, createFacesMessage(ex));
    }
    model.clear();
  }

  public ChangePasswordModel getModel() {
    return model;
  }

  public boolean isDone() {
    return done;
  }

  private static FacesMessage createFacesMessage(UserException ex) {
    return new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage());
  }
}
