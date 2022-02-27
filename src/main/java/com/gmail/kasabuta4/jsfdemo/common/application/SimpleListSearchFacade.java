package com.gmail.kasabuta4.jsfdemo.common.application;

import com.gmail.kasabuta4.jsfdemo.config.db.JsfDemoDB;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

public abstract class SimpleListSearchFacade<C extends Serializable, R extends Serializable> {

  @Inject @JsfDemoDB EntityManagerFactory emf;
  @Inject Logger logger;

  public List<R> search(C condition) throws ApplicationException {
    EntityManager em = createEntityManager(emf);
    try {
      em.getTransaction().begin();
      try {
        List<R> result = doSearch(em, condition);
        em.getTransaction().commit();
        return result;
      } catch (PersistenceException ex) {
        logger.log(Level.SEVERE, ex.getMessage(), ex);
        if (em.getTransaction().isActive()) {
          try {
            em.getTransaction().rollback();
          } catch (PersistenceException ignore) {
          }
        }
        throw new ApplicationException(ex);
      }
    } finally {
      em.close();
    }
  }

  protected EntityManager createEntityManager(EntityManagerFactory emf) {
    return emf.createEntityManager();
  }

  protected abstract List<R> doSearch(EntityManager em, C condition);
}
