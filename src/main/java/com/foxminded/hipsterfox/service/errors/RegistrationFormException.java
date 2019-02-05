package com.foxminded.hipsterfox.service.errors;

public class RegistrationFormException extends RuntimeException {

    public RegistrationFormException() {
        super();
    }

    public RegistrationFormException(String message) {
        super(message);
    }

    public RegistrationFormException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistrationFormException(Throwable cause) {
        super(cause);
    }

    protected RegistrationFormException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
