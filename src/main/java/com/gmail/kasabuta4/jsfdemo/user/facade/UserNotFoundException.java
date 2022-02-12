package com.gmail.kasabuta4.jsfdemo.user.facade;

import com.gmail.kasabuta4.jsfdemo.user.entity.UserException;

public class UserNotFoundException extends UserException {

  private static final String MESSAGE = "パスワードの変更ができませんでした";

  public UserNotFoundException() {
    super(MESSAGE);
  }
}
