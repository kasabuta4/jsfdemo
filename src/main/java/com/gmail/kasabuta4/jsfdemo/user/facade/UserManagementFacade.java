package com.gmail.kasabuta4.jsfdemo.user.facade;

import com.gmail.kasabuta4.jsfdemo.config.db.JsfDemoDB;
import com.gmail.kasabuta4.jsfdemo.user.entity.JsfDemoUser;
import com.gmail.kasabuta4.jsfdemo.user.entity.UserException;
import com.gmail.kasabuta4.jsfdemo.user.entity.WrongPasswordException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

@Dependent
public class UserManagementFacade {

  @Inject @JsfDemoDB EntityManagerFactory emf;
  @Inject Logger logger;

  public void addUser(AddUserModel model) throws UserException {
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      JsfDemoUser user = new JsfDemoUser(model.getName(), model.getFullname(), model.getGroup());
      em.persist(user);
      em.getTransaction().commit();
    } catch (EntityExistsException | RollbackException duplicateException) {
      if (em.getTransaction().isActive()) {
        try {
          em.getTransaction().rollback();
        } catch (PersistenceException pex) {
          logger.log(Level.SEVERE, pex.getMessage(), pex);
        }
      }
      throw new DupulicateUserIdException();
    } finally {
      em.close();
    }
  }

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
        } catch (RollbackException rex) {
          logger.log(Level.SEVERE, rex.getMessage(), rex);
          if (em.getTransaction().isActive()) {
            try {
              em.getTransaction().rollback();
            } catch (PersistenceException pex) {
            }
          }
        }
        throw uex;
      } catch (RollbackException rex) {
        if (em.getTransaction().isActive()) {
          try {
            em.getTransaction().rollback();
          } catch (PersistenceException pex) {
            logger.log(Level.SEVERE, pex.getMessage(), pex);
          }
        }
        throw new UserException(UserException.PASSWORD_CHANGE_FAILURE_MESSAGE, rex);
      } catch (PersistenceException pex) {
        logger.log(Level.SEVERE, pex.getMessage(), pex);
        if (em.getTransaction().isActive()) {
          try {
            em.getTransaction().rollback();
          } catch (PersistenceException repeated) {
          }
        }
        throw new UserException(UserException.PASSWORD_CHANGE_FAILURE_MESSAGE, pex);
      }
    } finally {
      em.close();
    }
  }
}
