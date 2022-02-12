package com.gmail.kasabuta4.jsfdemo.user.entity;

public class PasswordIncludingUserIdException extends IllegalPasswordException {

  private static final String MESSAGE = "パスワード中にユーザーIDを含めることはできません";

  public PasswordIncludingUserIdException() {
    super(MESSAGE);
  }
}
