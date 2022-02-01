package com.gmail.kasabuta4.jsfdemo.common.authentication;

import java.io.IOException;
import java.text.MessageFormat;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasicAuthenticationFilter extends HttpFilter {

  @Resource(lookup = "java:comp/env/realm")
  String realm;

  @Inject AuthenticationFacade facade;

  @Override
  public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    Credential credential = AuthorizationParser.parse(req.getHeader("Authorization"));

    if (credential == null) {
      responseUnauthorized(res);
      return;
    }

    BasicPrincipal principal = facade.authenticate(credential);
    if (principal == null) {
      responseUnauthorized(res);
    } else {
      chain.doFilter(new AuthenticatedHttpServletRequestWrapper(req, principal), res);
    }
  }

  private void responseUnauthorized(HttpServletResponse res) throws IOException {
    res.addHeader("WWW-Authenticate", MessageFormat.format("Basic realm=\"{0}\"", realm));
    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
  }
}
