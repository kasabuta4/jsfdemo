package com.gmail.kasabuta4.jsfdemo.user.facade;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import com.gmail.kasabuta4.jsfdemo.config.db.JsfDemoDB;
import com.gmail.kasabuta4.jsfdemo.user.entity.JsfDemoUser;
import com.gmail.kasabuta4.jsfdemo.user.entity.UserManagementException;
import java.util.Collection;
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
    } catch (EntityExistsException | RollbackException ex) {
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
          throw UserManagementException.passwordChangeNotFound();
        }

        if (!user.verifyPassword(credential.getCurrentPassword())) {
          throw UserManagementException.wrongPassword();
        }

        user.changePassword(credential.getNewPassword());
        em.flush();
        em.getTransaction().commit();
      } catch (UserManagementException ex) {
        try {
          em.getTransaction().commit();
        } catch (RollbackException pex) {
          logger.log(Level.SEVERE, pex.getMessage(), pex);
          if (em.getTransaction().isActive()) {
            try {
              em.getTransaction().rollback();
            } catch (PersistenceException ignore) {
            }
          }
        }
        throw ex;
      } catch (OptimisticLockException ex) {
        if (em.getTransaction().isActive()) {
          try {
            em.getTransaction().rollback();
          } catch (PersistenceException pex) {
            logger.log(Level.SEVERE, pex.getMessage(), pex);
          }
        }
        throw UserManagementException.passwordChangeConflicted();
      } catch (PersistenceException pex) {
        logger.log(Level.SEVERE, pex.getMessage(), pex);
        if (em.getTransaction().isActive()) {
          try {
            em.getTransaction().rollback();
          } catch (PersistenceException ignore) {
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
            em.createNamedQuery(JsfDemoUser.LIST_ALL_LOCKED_QUERY_NAME, JsfDemoUser.class)
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

  public void unlockUsers(Collection<String> userNames) throws UserManagementException {
    if (userNames.isEmpty()) throw UserManagementException.unlockNoUser();

    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      try {
        List<JsfDemoUser> usersToUnlock =
            em.createNamedQuery(JsfDemoUser.SEARCH_BY_NAMES, JsfDemoUser.class)
                .setParameter("names", userNames)
                .getResultList();
        if (usersToUnlock.size() != userNames.size()) {
          throw UserManagementException.unlockDeletedUser();
        }
        usersToUnlock.stream().forEach(JsfDemoUser::unlock);
        em.flush();
        em.getTransaction().commit();
      } catch (UserManagementException uex) {
        if (em.getTransaction().isActive()) {
          try {
            em.getTransaction().rollback();
          } catch (PersistenceException pex) {
            logger.log(Level.SEVERE, pex.getMessage(), pex);
          }
        }
        throw uex;
      } catch (OptimisticLockException | RollbackException rex) {
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

  public List<UserProfileDto> listAllUsers() {
    List<UserProfileDto> result = emptyList();
    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      try {
        result =
            em.createNamedQuery(JsfDemoUser.LIST_ALL_QUERY_NAME, JsfDemoUser.class)
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
      }
    } finally {
      em.close();
      return result;
    }
  }

  public void deleteUsers(Collection<String> userNames) throws UserManagementException {
    if (userNames.isEmpty()) throw UserManagementException.deleteNoUser();

    EntityManager em = emf.createEntityManager();
    try {
      em.getTransaction().begin();
      try {
        List<JsfDemoUser> usersToDelete =
            em.createNamedQuery(JsfDemoUser.SEARCH_BY_NAMES, JsfDemoUser.class)
                .setParameter("names", userNames)
                .getResultList();
        if (usersToDelete.size() != userNames.size()) {
          throw UserManagementException.alreadyDeleted();
        }
        usersToDelete.stream().forEach(em::remove);
        em.flush();
        em.getTransaction().commit();
      } catch (UserManagementException uex) {
        try {
          em.getTransaction().commit();
        } catch (PersistenceException pex) {
          logger.log(Level.SEVERE, pex.getMessage(), pex);
          if (em.getTransaction().isActive()) {
            try {
              em.getTransaction().rollback();
            } catch (PersistenceException ignore) {
            }
          }
        }
        throw uex;
      } catch (OptimisticLockException | RollbackException rex) {
        if (em.getTransaction().isActive()) {
          try {
            em.getTransaction().rollback();
          } catch (PersistenceException pex) {
            logger.log(Level.SEVERE, pex.getMessage(), pex);
          }
        }
        throw UserManagementException.deleteConflicted();
      } catch (PersistenceException pex) {
        logger.log(Level.SEVERE, pex.getMessage(), pex);
        if (em.getTransaction().isActive()) {
          try {
            em.getTransaction().rollback();
          } catch (PersistenceException ignore) {
          }
        }
        throw UserManagementException.deleteUsersFailure(pex);
      }
    } finally {
      em.close();
    }
  }
}
