package com.gmail.kasabuta4.jsfdemo.user.facade;

import com.gmail.kasabuta4.jsfdemo.user.entity.JsfDemoGroup;
import com.gmail.kasabuta4.jsfdemo.user.entity.JsfDemoUser;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AddUserModel implements Serializable {

  private static final long serialVersionUID = 1L;

  @NotNull
  @Size(min = 7, max = 7)
  @Pattern(regexp = "\\w*")
  private String name;

  @NotNull
  @Size(max = 50)
  private String fullname;

  @NotNull private JsfDemoGroup group;

  public AddUserModel() {}

  public static AddUserModel fromJsfDemoUser(JsfDemoUser user) {
    AddUserModel model = new AddUserModel();
    model.setName(user.getName());
    model.setFullname(user.getFullname());
    model.setGroup(user.getGroup());
    return model;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  public JsfDemoGroup getGroup() {
    return group;
  }

  public void setGroup(JsfDemoGroup group) {
    this.group = group;
  }
}
