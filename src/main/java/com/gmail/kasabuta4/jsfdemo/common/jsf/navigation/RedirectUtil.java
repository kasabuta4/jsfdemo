package com.gmail.kasabuta4.jsfdemo.common.jsf.navigation;

public class RedirectUtil {

  private static final String FACES_REDIRECT_OPTION = "faces-redirect=true";

  public static String redirectTo(String page) {
    return page + "?" + FACES_REDIRECT_OPTION;
  }
}
