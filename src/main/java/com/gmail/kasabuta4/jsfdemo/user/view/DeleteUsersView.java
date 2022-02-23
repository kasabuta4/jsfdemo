package com.gmail.kasabuta4.jsfdemo.user.view;

import com.gmail.kasabuta4.jsfdemo.user.entity.UserManagementException;
import com.gmail.kasabuta4.jsfdemo.user.facade.UserManagementFacade;
import com.gmail.kasabuta4.jsfdemo.user.facade.UserProfileDto;
import java.util.Collection;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class DeleteUsersView extends ListSelectConfirmPerformView<String, UserProfileDto> {

  private static final long serialVersionUID = 1L;

  @Inject transient UserManagementFacade facade;

  public DeleteUsersView() {
    super(UserProfileDto::getName);
  }

  protected List<UserProfileDto> listAllItems() {
    return facade.listAllUsers();
  }

  protected void doPerform(Collection<String> keysOfSelectedItems) throws UserManagementException {
    facade.deleteUsers(keysOfSelectedItems);
  }
}
