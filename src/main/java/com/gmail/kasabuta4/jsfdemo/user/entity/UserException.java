package com.gmail.kasabuta4.jsfdemo.user.entity;

public class UserException extends Exception {

  // Application Exception
  private static final String WRONG_PASSWORD_MESSAGE = "現在のパスワードが誤っています";
  private static final String PASSWORD_TOO_SIMPLE_MESSAGE =
      "パスワードは英小文字、英大文字、数字、記号の4種類のうち3種類以上利用し、8文字以上とする必要があります";
  private static final String RECENTLY_USED_PASSWORD_MESSAGE = "過去3世代のパスワードは利用できません";
  private static final String INCLUDES_USER_ID_MESSAGE = "パスワード中にユーザーIDを含めることはできません";
  private static final String PASSWORD_CONFIRMATION_FAILURE_MESSAGE =
      "変更後のパスワードと変更後のパスワードの確認が一致しません";
  private static final String ALREADY_EXISTS_MESSAGE = "同一の名前のユーザが既に登録されています";
  private static final String NOT_FOUND_MESSAGE = "パスワードの変更ができませんでした";
  private static final String UNLOCK_CONFLICTED_MESSAGE = "競合が発生したため、ロック解除をやり直してください";

  // for JPA Exception
  private static final String PASSWORD_CHANGE_FAILURE_MESSAGE = "パスワード変更に失敗しました";
  private static final String UNLOCK_USERS_FAILURE_MESSAGE = "ロック解除に失敗しました";

  private UserException(String message) {
    super(message);
  }

  private UserException(String message, Throwable cause) {
    super(message, cause);
  }

  public static UserException wrongPassword() {
    return new UserException(WRONG_PASSWORD_MESSAGE);
  }

  public static UserException passwordTooSimple() {
    return new UserException(PASSWORD_TOO_SIMPLE_MESSAGE);
  }

  public static UserException recentlyUsedPassword() {
    return new UserException(RECENTLY_USED_PASSWORD_MESSAGE);
  }

  public static UserException includesUserId() {
    return new UserException(INCLUDES_USER_ID_MESSAGE);
  }

  public static UserException passwordConfirmationFailure() {
    return new UserException(PASSWORD_CONFIRMATION_FAILURE_MESSAGE);
  }

  public static UserException alreadyExits() {
    return new UserException(ALREADY_EXISTS_MESSAGE);
  }

  public static UserException notFound() {
    return new UserException(NOT_FOUND_MESSAGE);
  }

  public static UserException unlockConflicted() {
    return new UserException(UNLOCK_CONFLICTED_MESSAGE);
  }

  public static UserException passwordChangeFailure(Throwable cause) {
    return new UserException(PASSWORD_CHANGE_FAILURE_MESSAGE, cause);
  }

  public static UserException unlockUsersFailure(Throwable cause) {
    return new UserException(UNLOCK_USERS_FAILURE_MESSAGE, cause);
  }
}
