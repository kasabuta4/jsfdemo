package com.gmail.kasabuta4.jsfdemo.user.entity;

public class UserException extends Exception {

  public static final String PASSWORD_CHANGE_FAILURE_MESSAGE = "パスワード変更に失敗しました";
  public static final String UNLOCK_USERS_FAILURE_MESSAGE = "ロック解除に失敗しました";

  public UserException(String message) {
    super(message);
  }

  public UserException(String message, Throwable cause) {
    super(message, cause);
  }
}
