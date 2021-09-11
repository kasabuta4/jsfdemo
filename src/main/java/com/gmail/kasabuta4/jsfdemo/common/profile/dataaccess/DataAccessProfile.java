package com.gmail.kasabuta4.jsfdemo.common.profile.dataaccess;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class DataAccessProfile implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private long id;

  private String targetClass;
  private String targetMethod;

  private Timestamp processStarted;
  private Timestamp processFinished;

  @Column(insertable = false)
  private Timestamp recorded;

  @Version private int version;

  public DataAccessProfile() {}

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof DataAccessProfile)) return false;

    final DataAccessProfile other = (DataAccessProfile) obj;
    return this.id == other.id;
  }

  @Override
  public int hashCode() {
    return Long.valueOf(id).hashCode();
  }

  @Override
  public String toString() {
    return "DataAccessProfile{"
        + "id="
        + id
        + ", targetClass="
        + targetClass
        + ", targetMethod="
        + targetMethod
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

  public String getTargetClass() {
    return targetClass;
  }

  public void setTargetClass(String targetClass) {
    this.targetClass = targetClass;
  }

  public String getTargetMethod() {
    return targetMethod;
  }

  public void setTargetMethod(String targetMethod) {
    this.targetMethod = targetMethod;
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
