package com.gmail.kasabuta4.jsfdemo.common.jsf.message;

import javax.faces.application.FacesMessage;

public class FacesMessageProducer {

  public static FacesMessage error(Throwable cause) {
    return new FacesMessage(FacesMessage.SEVERITY_ERROR, cause.getMessage(), cause.getMessage());
  }
}
