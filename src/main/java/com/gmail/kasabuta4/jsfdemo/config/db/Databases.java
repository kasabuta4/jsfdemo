package com.gmail.kasabuta4.jsfdemo.config.db;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class Databases {

  @Produces
  @JsfDemoDB
  @PersistenceContext(unitName = "jsfdemoPU")
  private EntityManager jsfDemoEntityManager;
}
