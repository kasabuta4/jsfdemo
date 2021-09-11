package com.gmail.kasabuta4.jsfdemo.common.profile.web;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(
    activationConfig = {
      @ActivationConfigProperty(
          propertyName = "destinationType",
          propertyValue = "javax.jms.Queue"),
      @ActivationConfigProperty(
          propertyName = "destinationLookup",
          propertyValue = "java:app/jms/PerformanceLogQueue"),
    })
public class PerformanceLogger implements MessageListener {

  @Inject private PerformanceLogDao dao;

  @Inject private Logger logger;

  @Override
  public void onMessage(Message message) {
    try {
      dao.save(message.getBody(PerformanceLog.class));
    } catch (JMSException e) {
      logger.log(Level.SEVERE, "fail reading PerformanceLog Message", e);
    }
  }
}
