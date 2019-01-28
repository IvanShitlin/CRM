package ru.shitlin.service.errors;

public class DuplicateAgreementException extends RuntimeException {

    public DuplicateAgreementException() {
        super();
    }

    public DuplicateAgreementException(String message) {
        super(message);
    }

    public DuplicateAgreementException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateAgreementException(Throwable cause) {
        super(cause);
    }

    protected DuplicateAgreementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
