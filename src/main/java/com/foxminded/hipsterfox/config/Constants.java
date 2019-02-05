package com.foxminded.hipsterfox.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";
    public static final String EMAIL_ADDRESS_REGEX = "[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "en";

    public static final String MULTIPART_MIME_TYPE = "multipart/*";
    public static final String HTML_MIME_TYPE = "text/html";
    public static final String PLAIN_TEXT_MIME_TYPE = "text/plain";

    private Constants() {
    }
}
