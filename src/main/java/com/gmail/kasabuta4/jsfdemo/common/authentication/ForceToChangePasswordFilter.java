package com.gmail.kasabuta4.jsfdemo.common.authentication;

import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForceToChangePasswordFilter extends HttpFilter {

  private static final String INIT_PARAMETER_NAME_CHANGE_PASSWORD_PAGE_URI =
      "change-password-page-uri";
  private static final Pattern LEADNING_WS_PATTERN = Pattern.compile("^\\s+");
  private static final Pattern TRAILING_WS_PATTERN = Pattern.compile("\\s+$");

  private String changePasswordPageURI;

  @Override
  public void init(FilterConfig config) throws ServletException {
    super.init(config);
    initChangePasswordPageURI(config);
  }

  @Override
  public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    BasicPrincipal principal = (BasicPrincipal) req.getUserPrincipal();
    if (principal.isForceToChangePassword() && !isGoingToChangePasswordPage(req)) {
      res.sendRedirect(changePasswordPageURI);
    } else {
      chain.doFilter(req, res);
    }
  }

  private void initChangePasswordPageURI(FilterConfig config) {
    changePasswordPageURI =
        config.getServletContext().getContextPath()
            + trim(config.getInitParameter(INIT_PARAMETER_NAME_CHANGE_PASSWORD_PAGE_URI));
  }

  private boolean isGoingToChangePasswordPage(HttpServletRequest req) {
    return changePasswordPageURI.equals(req.getRequestURI());
  }

  private String trim(String url) {
    String leadingTrimmed = LEADNING_WS_PATTERN.matcher(url).replaceAll("");
    return TRAILING_WS_PATTERN.matcher(leadingTrimmed).replaceAll("");
  }
}
