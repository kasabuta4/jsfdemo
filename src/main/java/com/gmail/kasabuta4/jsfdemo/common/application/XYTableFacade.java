package com.gmail.kasabuta4.jsfdemo.common.application;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import com.gmail.kasabuta4.jsfdemo.config.db.JsfDemoDB;
import java.io.Serializable;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

public abstract class XYTableFacade<P extends Serializable, E extends Serializable, X, Y> {

  @Inject @JsfDemoDB EntityManagerFactory emf;
  @Inject Logger logger;

  public Map<X, Map<Y, E>> search(P condition) throws ApplicationException {
    EntityManager em = createEntityManager(emf);
    try {
      em.getTransaction().begin();
      try {
        Map<X, Map<Y, E>> result =
            doSearch(em, condition)
                .collect(groupingBy(this::xOf, toMap(this::yOf, Function.identity())));
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

  protected abstract Stream<E> doSearch(EntityManager em, P condition);

  protected abstract X xOf(E entity);

  protected abstract Y yOf(E entity);
}
