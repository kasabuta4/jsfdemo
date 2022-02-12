package com.gmail.kasabuta4.jsfdemo.common.authentication;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthorizationFilter extends HttpFilter {

  private static final String RESOURCE_COLLECTION_TO_ALL_INIT_PARAMETER_NAME = "resources-to-all";
  private static final Pattern RESOURCE_COLLECTION_INIT_PARAMETER_NAME_PATTERN =
      Pattern.compile("^resources-for-");

  private static final Pattern LEADNING_WS_PATTERN = Pattern.compile("^\\s+");
  private static final Pattern TRAILING_WS_PATTERN = Pattern.compile("\\s+$");

  private Map<String, List<Pattern>> roleToResourceCollectionMap;

  @Override
  public void init(FilterConfig config) throws ServletException {
    super.init(config);
    initRoleToResourceCollectionMap(config);
  }

  @Override
  public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    if (isAuthorized(req)) {
      chain.doFilter(req, res);
    } else {
      res.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
  }

  private void initRoleToResourceCollectionMap(FilterConfig config) {
    roleToResourceCollectionMap =
        Collections.list(config.getInitParameterNames()).stream()
            .filter(s -> RESOURCE_COLLECTION_INIT_PARAMETER_NAME_PATTERN.matcher(s).find())
            .collect(
                toMap(
                    AuthorizationFilter::extractRoleName,
                    s ->
                        extractResourceCollection(
                            config.getServletContext().getContextPath(),
                            config.getInitParameter(s))));
    roleToResourceCollectionMap.keySet().stream()
        .forEach(
            key ->
                roleToResourceCollectionMap
                    .get(key)
                    .addAll(
                        0,
                        extractResourceCollection(
                            config.getServletContext().getContextPath(),
                            config.getInitParameter(
                                RESOURCE_COLLECTION_TO_ALL_INIT_PARAMETER_NAME))));
  }

  private boolean isAuthorized(HttpServletRequest req) {
    BasicPrincipal principal = (BasicPrincipal) req.getUserPrincipal();
    return roleToResourceCollectionMap.getOrDefault(principal.getGroup().toLowerCase(), emptyList())
        .stream()
        .anyMatch(pattern -> pattern.matcher(req.getRequestURI()).lookingAt());
  }

  private static String extractRoleName(String initParameterName) {
    return RESOURCE_COLLECTION_INIT_PARAMETER_NAME_PATTERN
        .matcher(initParameterName)
        .replaceAll("")
        .toLowerCase();
  }

  private static List<Pattern> extractResourceCollection(
      String contextPath, String initParameterValue) {
    return Arrays.asList(initParameterValue.split(",")).stream()
        .map(AuthorizationFilter::trim)
        .map(s -> contextPath + s)
        .map(s -> Pattern.compile(s, Pattern.LITERAL))
        .collect(toList());
  }

  private static String trim(String s) {
    String leadingTrimmed = LEADNING_WS_PATTERN.matcher(s).replaceAll("");
    return TRAILING_WS_PATTERN.matcher(leadingTrimmed).replaceAll("");
  }
}
