package com.gmail.kasabuta4.jsfdemo.user.entity;

public class WrongPasswordException extends UserException {

  private static final String MESSAGE = "現在のパスワードが誤っています";

  public WrongPasswordException() {
    super(MESSAGE);
  }
}
