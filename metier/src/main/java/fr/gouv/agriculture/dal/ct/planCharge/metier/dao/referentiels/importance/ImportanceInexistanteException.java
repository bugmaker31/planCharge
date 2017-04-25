package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;

/**
 * Created by frederic.danna on 22/04/2017.
 */
public class ImportanceInexistanteException extends ImportanceDaoException {

    public ImportanceInexistanteException(String codeImportance) {
        super("Importance inexistante : '" + codeImportance + "'.");
    }

}
