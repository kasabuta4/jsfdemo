package com.gmail.kasabuta4.jsfdemo.user.entity;

public class UserManagementException extends Exception {

  // Application Exception
  private static final String WRONG_PASSWORD_MESSAGE = "現在のパスワードが誤っています";
  private static final String PASSWORD_TOO_SIMPLE_MESSAGE =
      "パスワードは英小文字、英大文字、数字、記号の4種類のうち3種類以上利用し、8文字以上とする必要があります";
  private static final String RECENTLY_USED_PASSWORD_MESSAGE = "過去3世代のパスワードは利用できません";
  private static final String INCLUDES_USER_ID_MESSAGE = "パスワード中にユーザーIDを含めることはできません";
  private static final String PASSWORD_CONFIRMATION_FAILURE_MESSAGE =
      "変更後のパスワードと変更後のパスワードの確認が一致しません";
  private static final String ALREADY_EXISTS_MESSAGE = "同一の名前のユーザが既に登録されています";
  private static final String PASSWORD_CHANGE_NOT_FOUND_MESSAGE = "削除されたため、パスワード変更に失敗しました";
  private static final String PASSWORD_CHANGE_CONFLICTED_MESSAGE = "競合が発生したため、パスワード変更をやり直してください";
  private static final String UNLOCK_CONFLICTED_MESSAGE = "競合が発生したため、ロック解除をやり直してください";
  private static final String DELETE_NO_USER_MESSAGE = "削除するユーザーを選択してください";
  private static final String ALREADY_DELETED_MESSAGE = "一部のユーザが既に削除されたため、最初からやり直してください";
  private static final String DELETE_CONFLICTED_MESSAGE = "競合が発生したため、最初からやり直してください";

  // for JPA Exception
  private static final String PASSWORD_CHANGE_FAILURE_MESSAGE = "パスワード変更に失敗しました";
  private static final String UNLOCK_USERS_FAILURE_MESSAGE = "ロック解除に失敗しました";
  private static final String DELETE_USERS_FAILURE_MESSAGE = "ユーザ削除に失敗しました";

  private UserManagementException(String message) {
    super(message);
  }

  private UserManagementException(String message, Throwable cause) {
    super(message, cause);
  }

  public static UserManagementException wrongPassword() {
    return new UserManagementException(WRONG_PASSWORD_MESSAGE);
  }

  public static UserManagementException passwordTooSimple() {
    return new UserManagementException(PASSWORD_TOO_SIMPLE_MESSAGE);
  }

  public static UserManagementException recentlyUsedPassword() {
    return new UserManagementException(RECENTLY_USED_PASSWORD_MESSAGE);
  }

  public static UserManagementException includesUserId() {
    return new UserManagementException(INCLUDES_USER_ID_MESSAGE);
  }

  public static UserManagementException passwordConfirmationFailure() {
    return new UserManagementException(PASSWORD_CONFIRMATION_FAILURE_MESSAGE);
  }

  public static UserManagementException alreadyExits() {
    return new UserManagementException(ALREADY_EXISTS_MESSAGE);
  }

  public static UserManagementException passwordChangeNotFound() {
    return new UserManagementException(PASSWORD_CHANGE_NOT_FOUND_MESSAGE);
  }

  public static UserManagementException passwordChangeConflicted() {
    return new UserManagementException(PASSWORD_CHANGE_CONFLICTED_MESSAGE);
  }

  public static UserManagementException unlockConflicted() {
    return new UserManagementException(UNLOCK_CONFLICTED_MESSAGE);
  }

  public static UserManagementException deleteNoUser() {
    return new UserManagementException(DELETE_NO_USER_MESSAGE);
  }

  public static UserManagementException alreadyDeleted() {
    return new UserManagementException(ALREADY_DELETED_MESSAGE);
  }

  public static UserManagementException deleteConflicted() {
    return new UserManagementException(DELETE_CONFLICTED_MESSAGE);
  }

  public static UserManagementException passwordChangeFailure(Throwable cause) {
    return new UserManagementException(PASSWORD_CHANGE_FAILURE_MESSAGE, cause);
  }

  public static UserManagementException unlockUsersFailure(Throwable cause) {
    return new UserManagementException(UNLOCK_USERS_FAILURE_MESSAGE, cause);
  }

  public static UserManagementException deleteUsersFailure(Throwable cause) {
    return new UserManagementException(DELETE_USERS_FAILURE_MESSAGE, cause);
  }
}
