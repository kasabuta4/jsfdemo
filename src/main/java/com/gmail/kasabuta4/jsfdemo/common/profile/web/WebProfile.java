package com.gmail.kasabuta4.jsfdemo.common.profile.web;

import com.gmail.kasabuta4.jsfdemo.common.profile.Profile;
import javax.persistence.Entity;

@Entity
public class WebProfile extends Profile {

  private static final long serialVersionUID = 1L;

  private String remoteAddress;
  private String remoteHost;
  private String remoteUser;
  private String requestURI;
  private int responseStatus;

  public WebProfile() {}

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof WebProfile)) return false;

    final WebProfile other = (WebProfile) obj;
    return this.id == other.id;
  }

  @Override
  public int hashCode() {
    return Long.valueOf(id).hashCode();
  }

  @Override
  public String toString() {
    return "WebProfile{"
        + "id="
        + id
        + ", remoteAddress="
        + remoteAddress
        + ", remoteHost="
        + remoteHost
        + ", remoteUser="
        + remoteUser
        + ", requestURI="
        + requestURI
        + ", responseStatus="
        + responseStatus
        + ", processStarted="
        + processStarted
        + ", processFinished="
        + processFinished
        + ", recorded="
        + recorded
        + ", version="
        + version
        + '}';
  }

  public String getRemoteAddress() {
    return remoteAddress;
  }

  public void setRemoteAddress(String remoteAddress) {
    this.remoteAddress = remoteAddress;
  }

  public String getRemoteHost() {
    return remoteHost;
  }

  public void setRemoteHost(String remoteHost) {
    this.remoteHost = remoteHost;
  }

  public String getRemoteUser() {
    return remoteUser;
  }

  public void setRemoteUser(String remoteUser) {
    this.remoteUser = remoteUser;
  }

  public String getRequestURI() {
    return requestURI;
  }

  public void setRequestURI(String requestURI) {
    this.requestURI = requestURI;
  }

  public int getResponseStatus() {
    return responseStatus;
  }

  public void setResponseStatus(int responseStatus) {
    this.responseStatus = responseStatus;
  }
}
