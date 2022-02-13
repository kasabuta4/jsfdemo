package com.gmail.kasabuta4.jsfdemo.user.view;

import com.gmail.kasabuta4.jsfdemo.common.jsf.message.FacesMessageProducer;
import com.gmail.kasabuta4.jsfdemo.user.entity.UserManagementException;
import com.gmail.kasabuta4.jsfdemo.user.facade.CredentialDto;
import com.gmail.kasabuta4.jsfdemo.user.facade.UserManagementFacade;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
public class ChangePasswordView {

  @Inject UserManagementFacade facade;

  private CredentialDto credential = new CredentialDto();
  private boolean done;

  public void changePassword() {
    credential.setName(
        FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal().getName());
    try {
      facade.changePassword(credential);
      done = true;
    } catch (UserManagementException ex) {
      FacesContext.getCurrentInstance().addMessage(null, FacesMessageProducer.error(ex));
    }
    credential.clear();
  }

  public CredentialDto getCredential() {
    return credential;
  }

  public boolean isDone() {
    return done;
  }
}
