package com.foxminded.hipsterfox.service.errors;

public class LicenceNotAcceptedException extends RuntimeException {

    public LicenceNotAcceptedException() {
    }

    public LicenceNotAcceptedException(String message) {
        super(message);
    }

    public LicenceNotAcceptedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LicenceNotAcceptedException(Throwable cause) {
        super(cause);
    }

    public LicenceNotAcceptedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
