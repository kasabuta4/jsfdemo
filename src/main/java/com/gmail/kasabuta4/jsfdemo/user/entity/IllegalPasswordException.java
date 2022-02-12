package com.gmail.kasabuta4.jsfdemo.user.entity;

public class IllegalPasswordException extends UserException {

  private static final String MESSAGE = "不正なパスワードです";

  public IllegalPasswordException() {
    super(MESSAGE);
  }

  public IllegalPasswordException(String message) {
    super(message);
  }
}
