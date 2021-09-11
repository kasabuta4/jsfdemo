package com.gmail.kasabuta4.jsfdemo.common.profile.web;

import com.gmail.kasabuta4.jsfdemo.config.db.JsfDemoDB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class WebProfileDao {

  @Inject @JsfDemoDB private EntityManager em;

  @Transactional
  public void save(WebProfile log) {
    em.persist(log);
  }
}
