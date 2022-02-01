package com.gmail.kasabuta4.jsfdemo.common.authentication;

import java.io.Serializable;

class Credential implements Serializable {

  private static final long serialVersionUID = 1L;

  private String user;
  private String password;

  Credential() {}

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
