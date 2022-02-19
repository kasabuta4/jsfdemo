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
public class UnlockUsersView implements Serializable {

  @Inject transient UserManagementFacade facade;

  private List<UserProfileDto> lockedUsers;
  private List<UserProfileDto> selectedUsers;
  private List<UserProfileDto> unlockedUsers;
  private Map<String, Boolean> selectionStatus = new LinkedHashMap<>();

  public void listLockedUsers() {
    lockedUsers = facade.listLockedUsers();
  }

  public void updateNoUserSelected() {} // do nothing

  public void confirmUnloockingUsers() {
    Collection<String> selected = selectedNames();
    selectedUsers =
        lockedUsers.stream().filter(u -> selected.contains(u.getName())).collect(toList());
  }

  public void backToSelect() {
    selectedUsers = null;
  }

  public void unlockSelectedUsers() {
    try {
      facade.unlockUsers(selectedNames());
      unlockedUsers = selectedUsers;
    } catch (UserManagementException ex) {
      FacesContext.getCurrentInstance().addMessage(null, FacesMessageProducer.error(ex));
      listLockedUsers();
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
    return !isConfirming() && !isUnlocked();
  }

  public boolean isConfirming() {
    return !isUnlocked() && selectedUsers != null && !selectedUsers.isEmpty();
  }

  public boolean isUnlocked() {
    return unlockedUsers != null && !unlockedUsers.isEmpty();
  }

  public List<UserProfileDto> getLockedUsers() {
    return lockedUsers;
  }

  public List<UserProfileDto> getSelectedUsers() {
    return selectedUsers;
  }

  public List<UserProfileDto> getUnlockedUsers() {
    return unlockedUsers;
  }

  public Map<String, Boolean> getSelectionStatus() {
    return selectionStatus;
  }

  public boolean isNoUserSelected() {
    return !selectionStatus.values().stream().anyMatch(e -> e);
  }
}
