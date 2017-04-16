package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import fr.gouv.agriculture.dal.ct.planCharge.metier.MetierException;

/**
 * Created by frederic.danna on 16/04/2017.
 */
public class ServiceException extends MetierException {
    public ServiceException(String message) {
        super(message);
    }
}
