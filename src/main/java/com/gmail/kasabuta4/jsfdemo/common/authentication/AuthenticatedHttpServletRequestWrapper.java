package com.gmail.kasabuta4.jsfdemo.common.authentication;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class AuthenticatedHttpServletRequestWrapper extends HttpServletRequestWrapper {

  private BasicPrincipal principal;

  public AuthenticatedHttpServletRequestWrapper(
      HttpServletRequest request, BasicPrincipal principal) {
    super(request);
    this.principal = principal;
  }

  @Override
  public String getAuthType() {
    return BASIC_AUTH;
  }

  @Override
  public String getRemoteUser() {
    return principal.getName();
  }

  @Override
  public Principal getUserPrincipal() {
    return principal;
  }

  @Override
  public boolean isUserInRole(String role) {
    return principal.isUserInRole(role);
  }
}
