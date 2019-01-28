package ru.shitlin.web.rest.errors;

public class LicenceNotAcceptedException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public LicenceNotAcceptedException(String message, String entityName,  String errorKey) {
        super(message, entityName, errorKey);
    }
}
