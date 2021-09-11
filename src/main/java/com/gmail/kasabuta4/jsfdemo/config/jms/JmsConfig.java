package com.gmail.kasabuta4.jsfdemo.config.jms;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.jms.Destination;

@Dependent
public class JmsConfig {

  @Resource(lookup = "java:app/jms/PerformanceLogQueue")
  @Produces
  @PerformanceLogQueue
  private Destination performanceLogQueue;

  @Resource(lookup = "java:app/jms/DataAccessProfileQueue")
  @Produces
  @DataAccessProfileQueue
  private Destination dataAccessProfileQueue;

  JmsConfig() {}
}
