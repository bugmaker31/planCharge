package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;

/**
 * Created by frederic.danna on 22/04/2017.
 */
public class PlanChargeDaoException extends DaoException {
    public PlanChargeDaoException(String message) {
        super(message);
    }

    public PlanChargeDaoException(String message, Exception cause) {
        super(message, cause);
    }
}
