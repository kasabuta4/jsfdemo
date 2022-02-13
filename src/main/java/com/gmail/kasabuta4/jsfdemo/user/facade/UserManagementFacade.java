package com.gmail.kasabuta4.jsfdemo.user.facade;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import com.gmail.kasabuta4.jsfdemo.config.db.JsfDemoDB;
import com.gmail.kasabuta4.jsfdemo.user.entity.JsfDemoUser;
import com.gmail.kasabuta4.jsfdemo.user.entity.UserException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.OptimisticLockException;
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
      throw UserException.alreadyExits();
    } finally {
      em.close();
    }
  }

  public void changePassword(ChangePasswordModel model) throws UserException {
    if (!model.matchesConfirmationPasswrod()) {
      throw UserException.passwordConfirmationFailure();
    }

    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      try {
        JsfDemoUser user = em.find(JsfDemoUser.class, model.getName());

        if (user == null) {
          throw UserException.notFound();
        }

        if (!user.verifyPassword(model.getCurrentPassword())) {
          throw UserException.wrongPassword();
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
        throw UserException.passwordChangeFailure(rex);
      } catch (PersistenceException pex) {
        logger.log(Level.SEVERE, pex.getMessage(), pex);
        if (em.getTransaction().isActive()) {
          try {
            em.getTransaction().rollback();
          } catch (PersistenceException repeated) {
          }
        }
        throw UserException.passwordChangeFailure(pex);
      }
    } finally {
      em.close();
    }
  }

  public List<AddUserModel> listLockedUsers() {
    List<AddUserModel> result = emptyList();
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      try {
        result =
            em.createNamedQuery("listLockedUsers", JsfDemoUser.class)
                .getResultStream()
                .map(AddUserModel::fromJsfDemoUser)
                .collect(toList());
        em.getTransaction().commit();
      } catch (PersistenceException pex) {
        logger.log(Level.SEVERE, pex.getMessage(), pex);
        if (em.getTransaction().isActive()) {
          try {
            em.getTransaction().rollback();
          } catch (PersistenceException ignore) {
          }
        }
      } finally {
        return result;
      }
    } finally {
      em.close();
    }
  }

  public void unlockUsers(List<String> list) throws UserException {
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      try {
        em.createNamedQuery("listUnlockUsers", JsfDemoUser.class)
            .setParameter("unlockNameList", list)
            .getResultStream()
            .forEach(JsfDemoUser::unlock);
        em.flush();
        em.getTransaction().commit();
      } catch (OptimisticLockException ex) {
        if (em.getTransaction().isActive()) {
          try {
            em.getTransaction().rollback();
          } catch (PersistenceException pex) {
            logger.log(Level.SEVERE, pex.getMessage(), pex);
          }
        }
        throw UserException.unlockConflicted();
      } catch (PersistenceException pex) {
        logger.log(Level.SEVERE, pex.getMessage(), pex);
        if (em.getTransaction().isActive()) {
          try {
            em.getTransaction().rollback();
          } catch (PersistenceException ignore) {
          }
        }
        throw UserException.unlockUsersFailure(pex);
      }
    } finally {
      em.close();
    }
  }
}
