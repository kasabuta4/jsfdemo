package com.gmail.kasabuta4.jsfdemo.user.view;

import com.gmail.kasabuta4.jsfdemo.user.entity.JsfDemoGroup;
import com.gmail.kasabuta4.jsfdemo.user.entity.UserManagementException;
import com.gmail.kasabuta4.jsfdemo.user.facade.AddUserModel;
import com.gmail.kasabuta4.jsfdemo.user.facade.UserManagementFacade;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class AddUserView {

  @Inject UserManagementFacade facade;

  private AddUserModel form = new AddUserModel();
  private AddUserModel result;
  private final Map<String, JsfDemoGroup> groups = JsfDemoGroup.LABEL_TO_VALUE_MAP;

  public void addUser() {
    try {
      facade.addUser(form);
      result = form;
      form = new AddUserModel();
    } catch (UserManagementException ex) {
      FacesContext.getCurrentInstance().addMessage(null, createFacesMessage(ex));
    }
  }

  private static FacesMessage createFacesMessage(UserManagementException ex) {
    return new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage());
  }

  public AddUserModel getForm() {
    return form;
  }

  public AddUserModel getResult() {
    return result;
  }

  public Map<String, JsfDemoGroup> getGroups() {
    return groups;
  }
}
