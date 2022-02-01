package com.gmail.kasabuta4.jsfdemo.common.authentication;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.text.MessageFormat.format;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthorizationParser {

  private static final String REGEX_LWS = "(\\r\\n)?[ \\t]+";
  private static final String REGEX_BASE64_SEQUENCE = "[-+A-Za-z0-9]+={0,2}";

  //  production rule
  //    credentials = "Basic" basic-credentials
  //    basic-credentials = base64-user-pass
  //    base64-user-pass = <base64 encoding of user-pass,except not limited to 76 char/line>
  private static final Pattern CREDENTIALS_PATTERN =
      compile(
          format("({0})*Basic{0}({1})({0})*", REGEX_LWS, REGEX_BASE64_SEQUENCE), CASE_INSENSITIVE);

  private static final Pattern PREFIX_LWS_PATTERN = compile(format("^({0})*", REGEX_LWS));
  private static final Pattern SUFFIX_LWS_PATTERN = compile(format("({0})*$", REGEX_LWS));

  public static Credential parse(String credentials) {
    String base64UserPass = credentialsToBase64UserPass(credentials);
    String userPass = decodeBase64UserPass(base64UserPass);
    return userPassToCredential(userPass);
  }

  private static String credentialsToBase64UserPass(String credentials) {
    if (credentials == null) return null;

    Matcher credentialsMatcher = CREDENTIALS_PATTERN.matcher(credentials);
    return credentialsMatcher.matches() ? credentialsMatcher.group(4) : null;
  }

  private static String decodeBase64UserPass(String base64UserPass) {
    if (base64UserPass == null) return null;

    return new String(Base64.getDecoder().decode(base64UserPass), US_ASCII);
  }

  private static Credential userPassToCredential(String userPass) {
    if (userPass == null) return null;

    //  production rule
    //    user-pass = userid ":" password
    String[] userPassArray = userPass.split(":", 2);
    if (userPassArray.length < 2) return null;

    Credential credential = new Credential();
    credential.setUser(trimLWS(userPassArray[0]));
    credential.setPassword(trimLWS(userPassArray[1]));
    return credential;
  }

  private static String trimLWS(String original) {
    String trimmedLeading = PREFIX_LWS_PATTERN.matcher(original).replaceAll("");
    return SUFFIX_LWS_PATTERN.matcher(trimmedLeading).replaceAll("");
  }
}
