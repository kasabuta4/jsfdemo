package com.gmail.kasabuta4.jsfdemo.common.profile;

import com.gmail.kasabuta4.jsfdemo.config.db.JsfDemoDB;
import javax.inject.Inject;
import javax.persistence.EntityManager;

public abstract class ProfileDao<T extends Profile> {

  @Inject @JsfDemoDB protected EntityManager em;

  public void save(T profile) {
    em.persist(profile);
  }
}
