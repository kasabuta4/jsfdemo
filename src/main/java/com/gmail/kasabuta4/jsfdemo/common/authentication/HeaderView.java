package com.gmail.kasabuta4.jsfdemo.common.authentication;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@RequestScoped
public class HeaderView {

  public BasicPrincipal getPrincipal() {
    return (BasicPrincipal)
        FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
  }
}
