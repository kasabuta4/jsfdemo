package com.gmail.kasabuta4.jsfdemo.config.db;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

@ApplicationScoped
public class Databases {

  @Produces
  @JsfDemoDB
  @PersistenceContext(unitName = "jsfdemoPU")
  private EntityManager jsfDemoEntityManager;

  @Dependent
  @Produces
  @JsfDemoDB
  @PersistenceUnit(unitName = "jsfdemoPU2")
  private EntityManagerFactory jsfDemoEntityManagerFactory;
}
