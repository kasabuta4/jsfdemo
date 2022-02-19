package com.gmail.kasabuta4.jsfdemo.user.view;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import com.gmail.kasabuta4.jsfdemo.common.jsf.message.FacesMessageProducer;
import com.gmail.kasabuta4.jsfdemo.user.entity.UserManagementException;
import com.gmail.kasabuta4.jsfdemo.user.facade.UserManagementFacade;
import com.gmail.kasabuta4.jsfdemo.user.facade.UserProfileDto;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class DeleteUsersView implements Serializable {

  @Inject transient UserManagementFacade facade;

  private List<UserProfileDto> allUsers;
  private List<UserProfileDto> selectedUsers;
  private List<UserProfileDto> deletedUsers;
  private Map<String, Boolean> selectionStatus = new LinkedHashMap<>();

  public void listAllUsers() {
    allUsers = facade.listAllUsers();
  }

  public void updateNoUserSelected() {} // do nothing

  public void confirmDeletingUsers() {
    Collection<String> selected = selectedNames();
    selectedUsers = allUsers.stream().filter(u -> selected.contains(u.getName())).collect(toList());
  }

  public void backToSelect() {
    selectedUsers = null;
  }

  public void deleteSelectedUsers() {
    try {
      facade.deleteUsers(selectedNames());
      deletedUsers = selectedUsers;
    } catch (UserManagementException ex) {
      FacesContext.getCurrentInstance().addMessage(null, FacesMessageProducer.error(ex));
      listAllUsers();
      selectedUsers = null;
      selectionStatus.clear();
    }
  }

  private Collection<String> selectedNames() {
    return selectionStatus.entrySet().stream()
        .filter(Map.Entry::getValue)
        .map(Map.Entry::getKey)
        .collect(toSet());
  }

  public boolean isSelecting() {
    return !isConfirming() && !isDeleted();
  }

  public boolean isConfirming() {
    return !isDeleted() && selectedUsers != null && !selectedUsers.isEmpty();
  }

  public boolean isDeleted() {
    return deletedUsers != null && !deletedUsers.isEmpty();
  }

  public List<UserProfileDto> getAllUsers() {
    return allUsers;
  }

  public List<UserProfileDto> getSelectedUsers() {
    return selectedUsers;
  }

  public List<UserProfileDto> getDeletedUsers() {
    return deletedUsers;
  }

  public Map<String, Boolean> getSelectionStatus() {
    return selectionStatus;
  }

  public boolean isNoUserSelected() {
    return !selectionStatus.values().stream().anyMatch(e -> e);
  }
}
