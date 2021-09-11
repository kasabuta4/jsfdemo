package com.gmail.kasabuta4.jsfdemo.common.profile.dataaccess;

import com.gmail.kasabuta4.jsfdemo.config.db.JsfDemoDB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class DataAccessProfileDao {

  @Inject @JsfDemoDB private EntityManager em;

  @Transactional
  public void save(DataAccessProfile log) {
    em.persist(log);
  }
}
