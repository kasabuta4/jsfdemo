package com.gmail.kasabuta4.jsfdemo.common.profile;

import static javax.ejb.TransactionAttributeType.REQUIRED;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.TransactionAttribute;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public abstract class ProfileLogger<S extends Profile, T extends ProfileDao<S>>
    implements MessageListener {

  protected ProfileLogger() {}

  @Override
  @TransactionAttribute(REQUIRED)
  public void onMessage(Message message) {
    try {
      getProfileDao().save(message.getBody(getProfileClass()));
    } catch (JMSException e) {
      getLogger().log(Level.SEVERE, "fail reading Profile Message", e);
    }
  }

  protected abstract T getProfileDao();

  protected abstract Class<S> getProfileClass();

  protected abstract Logger getLogger();
}
