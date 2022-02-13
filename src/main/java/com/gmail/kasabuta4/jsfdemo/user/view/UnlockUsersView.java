package com.gmail.kasabuta4.jsfdemo.user.view;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import com.gmail.kasabuta4.jsfdemo.user.entity.UserException;
import com.gmail.kasabuta4.jsfdemo.user.facade.AddUserModel;
import com.gmail.kasabuta4.jsfdemo.user.facade.UserManagementFacade;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class UnlockUsersView {

  @Inject UserManagementFacade facade;

  private List<AddUserModel> lockedUsers;
  private List<AddUserModel> unlockedUsers;
  private Map<String, Boolean> status;

  @PostConstruct
  void init() {
    updateLockedUsers();
    updateStatus();
  }

  public void unlock() {
    List<String> selected = selectedNames();
    try {
      facade.unlockUsers(selected);
      updateUnlockedUsers(selected);
      updateLockedUsers();
      updateStatus();
    } catch (UserException ex) {
      FacesContext.getCurrentInstance().addMessage(null, createFacesMessage(ex));
    }
  }

  private List<String> selectedNames() {
    return status.entrySet().stream()
        .filter(Entry::getValue)
        .map(Entry::getKey)
        .sorted()
        .collect(toList());
  }

  private void updateLockedUsers() {
    lockedUsers = facade.listLockedUsers();
  }

  private void updateStatus() {
    status = lockedUsers.stream().collect(toMap(AddUserModel::getName, user -> Boolean.FALSE));
  }

  private void updateUnlockedUsers(List<String> namesToUnlock) {
    Set<String> namesToUnlockSet = new HashSet<>(namesToUnlock);
    unlockedUsers =
        lockedUsers.stream()
            .filter(user -> namesToUnlockSet.contains(user.getName()))
            .collect(toList());
  }

  private static FacesMessage createFacesMessage(UserException ex) {
    return new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), ex.getMessage());
  }

  public List<AddUserModel> getLockedUsers() {
    return lockedUsers;
  }

  public List<AddUserModel> getUnlockedUsers() {
    return unlockedUsers;
  }

  public Map<String, Boolean> getStatus() {
    return status;
  }
}
