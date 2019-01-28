package ru.shitlin.web.rest.errors;

public class ActiveContractExistenceException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public ActiveContractExistenceException() {
        super(ErrorConstants.ACTIVE_CONTRACT_EXISTENCE_TYPE, "Active contract already exists!", "contract", "contractExists");
    }
}
