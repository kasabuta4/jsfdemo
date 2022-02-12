package com.gmail.kasabuta4.jsfdemo.user.entity;

import java.util.regex.Pattern;

public class PasswordComplexityPolicy {

  private static final int MIN_PASSWORD_LENGTH = 8;
  private static final int MIN_CHARACTER_TYPES_IN_USE = 4;

  private static final Pattern ILLEGAL_PASSWORD_CHARACTER_PATTERN =
      Pattern.compile("[^\\p{Upper}\\p{Lower}\\p{Digit}\\p{Punct}]");

  private static final Pattern UPPER_ALPHA_PATTERN = Pattern.compile("\\p{Upper}");
  private static final Pattern LOWER_ALPHA_PATTERN = Pattern.compile("\\p{Lower}");
  private static final Pattern DIGIT_PATTERN = Pattern.compile("\\p{Digit}");
  private static final Pattern SPECIAL_CHAR_PATTERN = Pattern.compile("\\p{Punct}");

  public static boolean isComplexEnough(String newPassword) {
    return !isTooShortPasswordLength(newPassword)
        && !hasIllegalPasswordCharacter(newPassword)
        && !isTooShortCharacterTypesInUse(newPassword);
  }

  private static boolean isTooShortPasswordLength(String newPassword) {
    return newPassword.length() < MIN_PASSWORD_LENGTH;
  }

  private static boolean hasIllegalPasswordCharacter(String newPassword) {
    return hasCharacterClass(ILLEGAL_PASSWORD_CHARACTER_PATTERN, newPassword);
  }

  private static boolean isTooShortCharacterTypesInUse(String newPassword) {
    return characterTypesInUse(newPassword) < MIN_CHARACTER_TYPES_IN_USE;
  }

  private static int characterTypesInUse(String newPassword) {
    int result = 0;
    if (hasUpperAlphaChar(newPassword)) result++;
    if (hasLowerAlphaChar(newPassword)) result++;
    if (hasDigitChar(newPassword)) result++;
    if (hasSpecialChar(newPassword)) result++;
    return result;
  }

  private static boolean hasUpperAlphaChar(String newPassword) {
    return hasCharacterClass(UPPER_ALPHA_PATTERN, newPassword);
  }

  private static boolean hasLowerAlphaChar(String newPassword) {
    return hasCharacterClass(LOWER_ALPHA_PATTERN, newPassword);
  }

  private static boolean hasDigitChar(String newPassword) {
    return hasCharacterClass(DIGIT_PATTERN, newPassword);
  }

  private static boolean hasSpecialChar(String newPassword) {
    return hasCharacterClass(SPECIAL_CHAR_PATTERN, newPassword);
  }

  private static boolean hasCharacterClass(Pattern characterClass, String newPassword) {
    return characterClass.matcher(newPassword).find();
  }
}
