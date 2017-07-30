package fr.gouv.agriculture.dal.ct.metier.dto;

import fr.gouv.agriculture.dal.ct.metier.MetierException;

public class DTOException extends MetierException {

    public DTOException(String message) {
        super(message);
    }

    public DTOException(String message, Exception cause) {
        super(message, cause);
    }
}
