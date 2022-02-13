package com.gmail.kasabuta4.jsfdemo.common.authentication;

import com.gmail.kasabuta4.jsfdemo.config.db.JsfDemoDB;
import com.gmail.kasabuta4.jsfdemo.user.entity.JsfDemoUser;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

@Dependent
public class AuthenticationFacade {

  @Inject @JsfDemoDB EntityManagerFactory emf;
  @Inject Logger logger;

  public BasicPrincipal authenticate(Credential credential) {
    BasicPrincipal result = null;
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      try {
        JsfDemoUser user = em.find(JsfDemoUser.class, credential.getUser());

        if (user == null || user.isLocked() || user.isExpired()) {
          // do nothing
        } else if (user.verifyPassword(credential.getPassword())) {
          if (user.requiresResetLoginFailure()) {
            user.resetLoginFailure();
            em.flush();
          }
          result = new BasicPrincipal(user);
        } else {
          user.countUpLoginFailure();
          em.flush();
        }
        em.getTransaction().commit();
      } catch (RollbackException rex) {
        try {
          em.getTransaction().rollback();
        } catch (PersistenceException pex) {
          logger.log(Level.SEVERE, pex.getMessage(), pex);
        }
      } catch (PersistenceException pex) {
        logger.log(Level.SEVERE, pex.getMessage(), pex);
        try {
          em.getTransaction().rollback();
        } catch (PersistenceException reex) {
        }
      }
    } finally {
      em.close();
    }
    return result;
  }
}
