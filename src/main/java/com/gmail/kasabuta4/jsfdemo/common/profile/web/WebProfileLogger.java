package com.gmail.kasabuta4.jsfdemo.common.profile.web;

import com.gmail.kasabuta4.jsfdemo.common.profile.ProfileLogger;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;

@MessageDriven(
    activationConfig = {
      @ActivationConfigProperty(
          propertyName = "destinationType",
          propertyValue = "javax.jms.Queue"),
      @ActivationConfigProperty(
          propertyName = "destinationLookup",
          propertyValue = "java:app/jms/WebProfileQueue"),
    })
public class WebProfileLogger extends ProfileLogger<WebProfile, WebProfileDao> {

  @Inject private WebProfileDao profileDao;

  @Inject private Logger logger;

  public WebProfileLogger() {}

  @Override
  protected WebProfileDao getProfileDao() {
    return profileDao;
  }

  @Override
  protected Class<WebProfile> getProfileClass() {
    return WebProfile.class;
  }

  @Override
  protected Logger getLogger() {
    return logger;
  }
}
