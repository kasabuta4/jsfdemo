package com.gmail.kasabuta4.jsfdemo.user.facade;

import java.io.Serializable;
import java.util.Objects;

public class CredentialDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;
  private String currentPassword;
  private String newPassword;
  private String confirmationPassword;

  public CredentialDto() {}

  public boolean matchesConfirmationPasswrod() {
    return Objects.equals(newPassword, confirmationPassword);
  }

  public void clear() {
    name = null;
    currentPassword = null;
    newPassword = null;
    confirmationPassword = null;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCurrentPassword() {
    return currentPassword;
  }

  public void setCurrentPassword(String currentPassword) {
    this.currentPassword = currentPassword;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public String getConfirmationPassword() {
    return confirmationPassword;
  }

  public void setConfirmationPassword(String confirmationPassword) {
    this.confirmationPassword = confirmationPassword;
  }
}
