package com.gmail.kasabuta4.jsfdemo.common.authentication;

import java.security.Principal;
import java.time.LocalDate;

public class BasicPrincipal implements Principal {

  private final String name;
  private final String fullname;
  private final String group;
  private final LocalDate expiration;

  public BasicPrincipal(String name, String fullname, String group, LocalDate expiration) {
    this.name = name;
    this.fullname = fullname;
    this.group = group;
    this.expiration = expiration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || !(o instanceof BasicPrincipal)) return false;
    BasicPrincipal other = (BasicPrincipal) o;
    return name.equals(other.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public String toString() {
    return String.format(
        "BasicPrincipal{name=%1$s, fullname=%2$s, group=%3$s, expiration=%4$tF}",
        name, fullname, group, expiration);
  }

  @Override
  public String getName() {
    return name;
  }

  public boolean isUserInRole(String role) {
    if (role == null) return false;
    return role.equals(group);
  }

  public String getFullname() {
    return fullname;
  }

  public LocalDate getExpiration() {
    return expiration;
  }
}
