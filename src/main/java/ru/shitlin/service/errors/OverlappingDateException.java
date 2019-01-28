package ru.shitlin.service.errors;

public class OverlappingDateException extends RuntimeException {

    public OverlappingDateException() {
        super();
    }

    public OverlappingDateException(String message) {
        super(message);
    }

    public OverlappingDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public OverlappingDateException(Throwable cause) {
        super(cause);
    }

    protected OverlappingDateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
