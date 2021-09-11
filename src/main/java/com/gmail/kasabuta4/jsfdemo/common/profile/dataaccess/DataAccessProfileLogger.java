package com.gmail.kasabuta4.jsfdemo.common.profile.dataaccess;

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
          propertyValue = "java:app/jms/DataAccessProfileQueue"),
    })
public class DataAccessProfileLogger
    extends ProfileLogger<DataAccessProfile, DataAccessProfileDao> {

  @Inject private DataAccessProfileDao profileDao;

  @Inject private Logger logger;

  public DataAccessProfileLogger() {}

  @Override
  protected DataAccessProfileDao getProfileDao() {
    return profileDao;
  }

  @Override
  protected Class<DataAccessProfile> getProfileClass() {
    return DataAccessProfile.class;
  }

  @Override
  protected Logger getLogger() {
    return logger;
  }
}
