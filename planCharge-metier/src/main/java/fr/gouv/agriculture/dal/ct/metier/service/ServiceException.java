package fr.gouv.agriculture.dal.ct.metier.service;

import fr.gouv.agriculture.dal.ct.metier.MetierException;

/**
 * Created by frederic.danna on 16/04/2017.
 */
public class ServiceException extends MetierException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Exception cause) {
        super(message, cause);
    }
}
