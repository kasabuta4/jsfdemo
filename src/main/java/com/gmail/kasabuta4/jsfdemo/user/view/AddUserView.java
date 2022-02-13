package com.gmail.kasabuta4.jsfdemo.user.view;

import com.gmail.kasabuta4.jsfdemo.common.jsf.message.FacesMessageProducer;
import com.gmail.kasabuta4.jsfdemo.user.entity.JsfDemoGroup;
import com.gmail.kasabuta4.jsfdemo.user.entity.UserManagementException;
import com.gmail.kasabuta4.jsfdemo.user.facade.UserManagementFacade;
import com.gmail.kasabuta4.jsfdemo.user.facade.UserProfileDto;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class AddUserView {

  @Inject UserManagementFacade facade;

  private UserProfileDto userToAdd = new UserProfileDto();
  private UserProfileDto addedUser;
  private final Map<String, JsfDemoGroup> groups = JsfDemoGroup.LABEL_TO_VALUE_MAP;

  public void addUser() {
    try {
      facade.addUser(userToAdd);
      addedUser = userToAdd;
      userToAdd = new UserProfileDto();
    } catch (UserManagementException ex) {
      FacesContext.getCurrentInstance().addMessage(null, FacesMessageProducer.error(ex));
    }
  }

  public UserProfileDto getUserToAdd() {
    return userToAdd;
  }

  public UserProfileDto getAddedUser() {
    return addedUser;
  }

  public Map<String, JsfDemoGroup> getGroups() {
    return groups;
  }
}
