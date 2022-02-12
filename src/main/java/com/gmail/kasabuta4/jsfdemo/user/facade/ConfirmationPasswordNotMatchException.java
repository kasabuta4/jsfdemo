package com.gmail.kasabuta4.jsfdemo.user.facade;

import com.gmail.kasabuta4.jsfdemo.user.entity.UserException;

public class ConfirmationPasswordNotMatchException extends UserException {

  private static final String MESSAGE = "変更後のパスワードと変更後のパスワードの確認が一致しません";

  public ConfirmationPasswordNotMatchException() {
    super(MESSAGE);
  }
}
