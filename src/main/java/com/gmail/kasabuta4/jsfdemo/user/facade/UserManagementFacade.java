package com.gmail.kasabuta4.jsfdemo.user.facade;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import com.gmail.kasabuta4.jsfdemo.config.db.JsfDemoDB;
import com.gmail.kasabuta4.jsfdemo.user.entity.JsfDemoUser;
import com.gmail.kasabuta4.jsfdemo.user.entity.UserManagementException;
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

  public void addUser(UserProfileDto profile) throws UserManagementException {
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      em.persist(profile.toJsfDemoUser());
      em.getTransaction().commit();
    } catch (EntityExistsException | RollbackException duplicateException) {
      if (em.getTransaction().isActive()) {
        try {
          em.getTransaction().rollback();
        } catch (PersistenceException pex) {
          logger.log(Level.SEVERE, pex.getMessage(), pex);
        }
      }
      throw UserManagementException.alreadyExits();
    } finally {
      em.close();
    }
  }

  public void changePassword(CredentialDto credential) throws UserManagementException {
    if (!credential.matchesConfirmationPasswrod()) {
      throw UserManagementException.passwordConfirmationFailure();
    }

    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      try {
        JsfDemoUser user = em.find(JsfDemoUser.class, credential.getName());

        if (user == null) {
          throw UserManagementException.notFound();
        }

        if (!user.verifyPassword(credential.getCurrentPassword())) {
          throw UserManagementException.wrongPassword();
        }

        user.changePassword(credential.getNewPassword());
        em.flush();
        em.getTransaction().commit();
      } catch (UserManagementException uex) {
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
        throw UserManagementException.passwordChangeFailure(rex);
      } catch (PersistenceException pex) {
        logger.log(Level.SEVERE, pex.getMessage(), pex);
        if (em.getTransaction().isActive()) {
          try {
            em.getTransaction().rollback();
          } catch (PersistenceException repeated) {
          }
        }
        throw UserManagementException.passwordChangeFailure(pex);
      }
    } finally {
      em.close();
    }
  }

  public List<UserProfileDto> listLockedUsers() {
    List<UserProfileDto> result = emptyList();
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      try {
        result =
            em.createNamedQuery("listLockedUsers", JsfDemoUser.class)
                .getResultStream()
                .map(UserProfileDto::fromJsfDemoUser)
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

  public void unlockUsers(List<String> list) throws UserManagementException {
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
        throw UserManagementException.unlockConflicted();
      } catch (PersistenceException pex) {
        logger.log(Level.SEVERE, pex.getMessage(), pex);
        if (em.getTransaction().isActive()) {
          try {
            em.getTransaction().rollback();
          } catch (PersistenceException ignore) {
          }
        }
        throw UserManagementException.unlockUsersFailure(pex);
      }
    } finally {
      em.close();
    }
  }
}
