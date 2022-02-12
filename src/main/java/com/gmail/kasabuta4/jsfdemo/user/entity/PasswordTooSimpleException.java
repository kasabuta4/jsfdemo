package com.gmail.kasabuta4.jsfdemo.user.entity;

public class PasswordTooSimpleException extends IllegalPasswordException {

  private static final String MESSAGE = "パスワードは英小文字、英大文字、数字、記号の4種類のうち3種類以上利用し、8文字以上とする必要があります";

  public PasswordTooSimpleException() {
    super(MESSAGE);
  }
}
