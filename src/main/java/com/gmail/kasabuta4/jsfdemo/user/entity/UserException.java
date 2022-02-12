package com.gmail.kasabuta4.jsfdemo.user.entity;

public class UserException extends Exception {

  private static final String MESSAGE = "パスワード変更に失敗しました";

  public UserException(String message) {
    super(message);
  }

  public UserException(Throwable cause) {
    super(MESSAGE, cause);
  }
}
