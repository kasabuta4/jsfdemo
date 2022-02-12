package com.gmail.kasabuta4.jsfdemo.user.facade;

import com.gmail.kasabuta4.jsfdemo.config.db.JsfDemoDB;
import com.gmail.kasabuta4.jsfdemo.user.entity.JsfDemoUser;
import com.gmail.kasabuta4.jsfdemo.user.entity.UserException;
import com.gmail.kasabuta4.jsfdemo.user.entity.WrongPasswordException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

@Dependent
public class UserManagementFacade {

  @Inject @JsfDemoDB EntityManagerFactory emf;
  @Inject Logger logger;

  public void changePassword(ChangePasswordModel model) throws UserException {
    if (!model.matchesConfirmationPasswrod()) {
      throw new ConfirmationPasswordNotMatchException();
    }

    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      try {
        JsfDemoUser user = em.find(JsfDemoUser.class, model.getName());

        if (user == null) {
          throw new UserNotFoundException();
        }

        if (!user.verifyPassword(model.getCurrentPassword())) {
          throw new WrongPasswordException();
        }

        user.changePassword(model.getNewPassword());
        em.flush();
        em.getTransaction().commit();
      } catch (UserException uex) {
        try {
          em.getTransaction().commit();
        } catch(RollbackException rex) {
          logger.log(Level.SEVERE, rex.getMessage(), rex);
          try {
            em.getTransaction().rollback();
          } catch(PersistenceException pex) {}
        }
        throw uex;
      } catch (RollbackException rex) {
        try {
          em.getTransaction().rollback();
        } catch (PersistenceException pex) {
          logger.log(Level.SEVERE, pex.getMessage(), pex);
        }
        throw new UserException(rex);
      } catch (PersistenceException pex) {
        logger.log(Level.SEVERE, pex.getMessage(), pex);
        try {
          em.getTransaction().rollback();
        } catch(PersistenceException reex) {}
        throw new UserException(pex);
      }
    } finally {
      em.close();
    }
  }
}
