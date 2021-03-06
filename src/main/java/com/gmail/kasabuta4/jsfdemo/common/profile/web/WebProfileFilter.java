package com.gmail.kasabuta4.jsfdemo.common.profile.web;

import com.gmail.kasabuta4.jsfdemo.config.jms.WebProfileQueue;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.regex.Pattern;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebProfileFilter implements Filter {

  private Pattern FACES_RESOURCE_REQUEST_PATTERN = Pattern.compile("javax.faces.resource");

  @Inject JMSContext jmsContext;

  @Inject @WebProfileQueue Destination webProfileQueue;

  public WebProfileFilter() {}

  @Override
  public void init(FilterConfig filterConfig) {}

  @Override
  public void destroy() {}

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    Timestamp processStarted = Timestamp.from(Instant.now());

    chain.doFilter(request, response);

    HttpServletRequest httpRequest = (HttpServletRequest) request;

    if (!FACES_RESOURCE_REQUEST_PATTERN.matcher(httpRequest.getRequestURI()).find()) {
      Timestamp processFinished = Timestamp.from(Instant.now());

      WebProfile profile = new WebProfile();
      profile.setRemoteAddress(httpRequest.getRemoteAddr());
      profile.setRemoteHost(httpRequest.getRemoteHost());
      profile.setRemoteUser(httpRequest.getRemoteUser());
      profile.setRequestURI(httpRequest.getRequestURI());
      profile.setResponseStatus(((HttpServletResponse) response).getStatus());
      profile.setProcessStarted(processStarted);
      profile.setProcessFinished(processFinished);
      jmsContext.createProducer().send(webProfileQueue, profile);
    }
  }
}
