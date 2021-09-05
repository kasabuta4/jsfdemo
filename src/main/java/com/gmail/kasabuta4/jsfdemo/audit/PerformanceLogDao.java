package com.gmail.kasabuta4.jsfdemo.audit;

import com.gmail.kasabuta4.jsfdemo.config.db.JsfDemoDB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class PerformanceLogDao {

  @Inject @JsfDemoDB private EntityManager em;

  @Transactional
  public void save(PerformanceLog log) {
    em.persist(log);
  }
}
