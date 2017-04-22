package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;

/**
 * Created by frederic.danna on 22/04/2017.
 */
public class ImportanceDaoException  extends DaoException {

    public ImportanceDaoException(String message) {
        super(message);
    }

    public ImportanceDaoException(String message, Exception cause) {
        super(message, cause);
    }
}
