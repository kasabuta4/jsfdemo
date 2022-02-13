package com.gmail.kasabuta4.jsfdemo.user.entity;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toMap;

import java.util.Map;
import java.util.function.Function;

public enum JsfDemoGroup {
  USER("ユーザー"),
  ADMIN("管理者");

  public static final Map<String, JsfDemoGroup> LABEL_TO_VALUE_MAP =
      unmodifiableMap(
          asList(values()).stream().collect(toMap(JsfDemoGroup::getLabel, Function.identity())));

  private final String label;

  JsfDemoGroup(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }
}
