package com.gmail.kasabuta4.jsfdemo.common.authentication;

import static java.time.temporal.ChronoUnit.DAYS;

import java.security.Principal;
import java.time.LocalDate;

public class BasicPrincipal implements Principal {

  private static final int DAYS_FORCE_TO_CHANGE_PASSWORD = 14;
  private static final int DAYS_RECOMMEND_TO_CHANGE_PASSWORD = 28;

  private final String name;
  private final String fullname;
  private final String group;
  private final LocalDate expiration;

  public BasicPrincipal(JsfDemoUser user) {
    this.name = user.getName();
    this.fullname = user.getFullname();
    this.group = user.getGroup();
    this.expiration = user.getExpiration();
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

  public String getGroup() {
    return group;
  }

  private int getRemainingDaysUntilExpiration() {
    return (int) DAYS.between(LocalDate.now(), expiration);
  }

  public int getRemainingDaysUntilForceToChangePassword() {
    return (int) getRemainingDaysUntilExpiration() - DAYS_FORCE_TO_CHANGE_PASSWORD;
  }

  public boolean isForceToChangePassword() {
    return getRemainingDaysUntilExpiration() <= DAYS_FORCE_TO_CHANGE_PASSWORD;
  }

  public boolean isReccomendToChangePassword() {
    return getRemainingDaysUntilExpiration() <= DAYS_RECOMMEND_TO_CHANGE_PASSWORD;
  }
}
