package com.gmail.kasabuta4.jsfdemo.common.profile.dataaccess;

import com.gmail.kasabuta4.jsfdemo.common.profile.Profile;
import javax.persistence.Entity;

@Entity
public class DataAccessProfile extends Profile {

  private static final long serialVersionUID = 1L;

  private String targetClass;
  private String targetMethod;

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
}
