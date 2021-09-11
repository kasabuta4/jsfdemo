package com.gmail.kasabuta4.jsfdemo.common.profile;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class Profile implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected long id;

  protected Timestamp processStarted;
  protected Timestamp processFinished;

  @Column(insertable = false)
  protected Timestamp recorded;

  @Version protected int version;

  protected Profile() {}

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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
