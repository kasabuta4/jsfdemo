package com.gmail.kasabuta4.jsfdemo.user.entity;

public class RecentlyUsedPasswordException extends IllegalPasswordException {

  private static final String MESSAGE = "過去3世代のパスワードは利用できません";

  public RecentlyUsedPasswordException() {
    super(MESSAGE);
  }
}
