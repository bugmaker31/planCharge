package fr.gouv.agriculture.dal.ct.metier.dao;

import fr.gouv.agriculture.dal.ct.metier.MetierException;

/**
 * Created by frederic.danna on 16/04/2017.
 */
public class DaoException extends MetierException {

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Exception cause) {
        super(message, cause);
    }
}
