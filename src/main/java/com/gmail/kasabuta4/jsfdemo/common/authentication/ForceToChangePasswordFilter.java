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
  private static final String INIT_PARAMETER_NAME_FACES_RESOURCE_REQUEST_URI =
      "faces-resource-request-uri";
  private static final Pattern LEADNING_WS_PATTERN = Pattern.compile("^\\s+");
  private static final Pattern TRAILING_WS_PATTERN = Pattern.compile("\\s+$");

  private String changePasswordPageURI;
  private Pattern facesRresourceRequestURIPattern;

  @Override
  public void init(FilterConfig config) throws ServletException {
    super.init(config);
    initChangePasswordPageURI(config);
    initFacesResourceRequestURIPattern(config);
  }

  @Override
  public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    BasicPrincipal principal = (BasicPrincipal) req.getUserPrincipal();
    if (principal.isForceToChangePassword() && !excludesFiltering(req)) {
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

  private void initFacesResourceRequestURIPattern(FilterConfig config) {
    String facesRresourceRequestURI =
        config.getServletContext().getContextPath()
            + trim(config.getInitParameter(INIT_PARAMETER_NAME_FACES_RESOURCE_REQUEST_URI));
    facesRresourceRequestURIPattern = Pattern.compile(facesRresourceRequestURI, Pattern.LITERAL);
  }

  private boolean excludesFiltering(HttpServletRequest req) {
    return changePasswordPageURI.equals(req.getRequestURI())
        || facesRresourceRequestURIPattern.matcher(req.getRequestURI()).lookingAt();
  }

  private static String trim(String url) {
    String leadingTrimmed = LEADNING_WS_PATTERN.matcher(url).replaceAll("");
    return TRAILING_WS_PATTERN.matcher(leadingTrimmed).replaceAll("");
  }
}
