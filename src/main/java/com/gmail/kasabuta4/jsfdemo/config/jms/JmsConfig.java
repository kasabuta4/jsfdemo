package com.gmail.kasabuta4.jsfdemo.config.jms;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.jms.Destination;

@Dependent
public class JmsConfig {

  @Resource(lookup = "java:app/jms/WebProfileQueue")
  @Produces
  @WebProfileQueue
  private Destination webProfileQueue;

  @Resource(lookup = "java:app/jms/DataAccessProfileQueue")
  @Produces
  @DataAccessProfileQueue
  private Destination dataAccessProfileQueue;

  JmsConfig() {}
}
