package com.gmail.kasabuta4.jsfdemo.user.facade;

import com.gmail.kasabuta4.jsfdemo.user.entity.UserException;

public class DupulicateUserIdException extends UserException {

  private static final String MESSAGE = "同一の名前のユーザが既に登録されています";

  public DupulicateUserIdException() {
    super(MESSAGE);
  }
}
