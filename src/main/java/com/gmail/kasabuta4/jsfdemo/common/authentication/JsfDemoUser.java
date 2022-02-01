package com.gmail.kasabuta4.jsfdemo.common.authentication;

import static java.nio.charset.StandardCharsets.US_ASCII;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "JSF_DEMO_USER")
public class JsfDemoUser implements Serializable {

  private static final long serialVersionUID = 1L;

  private static final String INITIAL_PASSWORD = "Jsf2Demo#";

  private static final int INITIAL_PASSWORD_VALID_PERIOD = 5;
  private static final int CHANGED_PASSWORD_VALID_PERIOD = 90;

  private static final String PASSWORD_DIGEST_ALGORHYTHM = "SHA-256";

  @Id private String name;

  @Basic(optional = false)
  private String fullname;

  @Basic(optional = false)
  @Column(name = "USER_GROUP")
  private String group;

  @Basic(optional = false)
  private String password;

  @Basic(optional = false)
  @Column(name = "PASSWORD_HISTORY_1")
  private String passwordHistory1;

  @Basic(optional = false)
  @Column(name = "PASSWORD_HISTORY_2")
  private String passwordHistory2;

  @Basic(optional = false)
  @Column(name = "CONSECUTIVE_LOGIN_FAILURE")
  private int consecutiveLoginFailure;

  @Basic(optional = false)
  private LocalDate expiration;

  @Version private Timestamp updated;

  public JsfDemoUser() {}

  public JsfDemoUser(String name, String fullname, String group) {
    this.name = name;
    this.fullname = fullname;
    this.group = group;
    initPassword();
    doResetLoginFailure();
    initExpiration();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || !(o instanceof JsfDemoUser)) return false;
    final JsfDemoUser other = (JsfDemoUser) o;
    return Objects.equals(this.name, other.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }

  public boolean verifyPassword(String password) {
    return Objects.equals(this.password, hashPassword(password));
  }

  public boolean validateNewPassword(String newPassword) {
    return PasswordComplexityPolicy.isComplexEnough(newPassword)
        && differentFromRecentPasswords(hashPassword(newPassword));
  }

  public void changePassword(String newPassword) {
    shiftPassword(hashPassword(newPassword));
    doResetLoginFailure();
    updateExpiration();
  }

  private void initPassword() {
    password = hashPassword(INITIAL_PASSWORD);
    passwordHistory1 = password;
    passwordHistory2 = password;
  }

  private void shiftPassword(String hashedNewPassword) {
    passwordHistory2 = passwordHistory1;
    passwordHistory1 = password;
    password = hashedNewPassword;
  }

  private boolean differentFromRecentPasswords(String hashedNewPassword) {
    Set<String> passwordHistory =
        new HashSet<>(Arrays.asList(password, passwordHistory1, passwordHistory2));
    return !passwordHistory.contains(hashedNewPassword);
  }

  private static String hashPassword(String password) {
    try {
      MessageDigest md = MessageDigest.getInstance(PASSWORD_DIGEST_ALGORHYTHM);
      md.update(password.getBytes(US_ASCII));
      return new String(Base64.getEncoder().encode(md.digest()), US_ASCII);
    } catch (NoSuchAlgorithmException ex) {
      return null; // never happens
    }
  }

  public void unlock() {
    shiftPassword(hashPassword(INITIAL_PASSWORD));
    doResetLoginFailure();
    initExpiration();
  }

  public void resetLoginFailure() {
    doResetLoginFailure();
  }

  public void countUpLoginFailure() {
    consecutiveLoginFailure++;
  }

  public boolean isLocked() {
    return consecutiveLoginFailure > 3;
  }

  public boolean requiresResetLoginFailure() {
    return consecutiveLoginFailure > 0;
  }

  private void doResetLoginFailure() {
    consecutiveLoginFailure = 0;
  }

  private void initExpiration() {
    expiration = LocalDate.now().plusDays(INITIAL_PASSWORD_VALID_PERIOD);
  }

  private void updateExpiration() {
    expiration = LocalDate.now().plusDays(CHANGED_PASSWORD_VALID_PERIOD);
  }

  public boolean isExpired() {
    return LocalDate.now().isAfter(expiration);
  }

  public BasicPrincipal createBasicPrincipal() {
    return new BasicPrincipal(name, fullname, group, expiration);
  }

  public String getName() {
    return name;
  }

  public String getFullname() {
    return fullname;
  }

  public String getGroup() {
    return group;
  }
}
