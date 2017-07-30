package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.tache;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;

/**
 * Created by frederic.danna on 22/04/2017.
 */
public class TacheDaoException extends DaoException {

    public TacheDaoException(String message) {
        super(message);
    }

    public TacheDaoException(String message, Exception cause) {
        super(message, cause);
    }
}
