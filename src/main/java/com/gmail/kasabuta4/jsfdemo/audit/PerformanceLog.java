package com.gmail.kasabuta4.jsfdemo.audit;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class PerformanceLog implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private long id;

  private String remoteAddress;
  private String remoteHost;
  private String remoteUser;
  private String requestURI;
  private int responseStatus;
  private Timestamp processStarted;
  private Timestamp processFinished;

  @Column(insertable = false)
  private Timestamp recorded;

  @Version private int version;

  public PerformanceLog() {}

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof PerformanceLog)) return false;

    final PerformanceLog other = (PerformanceLog) obj;
    return this.id == other.id;
  }

  @Override
  public int hashCode() {
    return Long.valueOf(id).hashCode();
  }

  @Override
  public String toString() {
    return "PerformanceLog{"
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

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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

  public Timestamp getProcessStarted() {
    return processStarted;
  }

  public void setProcessStarted(Timestamp processStarted) {
    this.processStarted = processStarted;
  }

  public Timestamp getProcessFinished() {
    return processFinished;
  }

  public void setProcessFinished(Timestamp processFinished) {
    this.processFinished = processFinished;
  }

  public Timestamp getRecorded() {
    return recorded;
  }

  public void setRecorded(Timestamp recorded) {
    this.recorded = recorded;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }
}
