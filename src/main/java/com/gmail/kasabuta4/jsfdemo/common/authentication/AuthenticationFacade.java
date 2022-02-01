package com.gmail.kasabuta4.jsfdemo.common.authentication;

import com.gmail.kasabuta4.jsfdemo.config.db.JsfDemoDB;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Dependent
public class AuthenticationFacade {

  @Inject @JsfDemoDB EntityManager em;

  @Transactional
  public BasicPrincipal authenticate(Credential credential) {
    JsfDemoUser user = em.find(JsfDemoUser.class, credential.getUser());
    if (user == null || user.isLocked() || user.isExpired()) return null;
    if (user.verifyPassword(credential.getPassword())) {
      if (user.requiresResetLoginFailure()) {
        user.resetLoginFailure();
        em.flush();
      }
      return user.createBasicPrincipal();
    } else {
      user.countUpLoginFailure();
      em.flush();
      return null;
    }
  }
}
