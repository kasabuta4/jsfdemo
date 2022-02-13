package com.gmail.kasabuta4.jsfdemo.user.facade;

import com.gmail.kasabuta4.jsfdemo.user.entity.UserException;

public class UnlockFailureException extends UserException {

  private static final String MESSAGE = "競合が発生したため、ロック解除をやり直してください";

  public UnlockFailureException() {
    super(MESSAGE);
  }
}
