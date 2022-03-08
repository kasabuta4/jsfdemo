package com.gmail.kasabuta4.jsfdemo.common.application;

import com.gmail.kasabuta4.jsfdemo.config.db.JsfDemoDB;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

public abstract class SimpleSearchFacade<C, R, E> {

  @Inject @JsfDemoDB EntityManagerFactory emf;
  @Inject Logger logger;

  public R search(C condition) throws ApplicationException {
    EntityManager em = createEntityManager(emf);
    try {
      em.getTransaction().begin();
      try {
        R result = convert(doSearch(em, condition));
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

  protected abstract Stream<E> doSearch(EntityManager em, C condition);

  protected abstract R convert(Stream<E> stream);
}
