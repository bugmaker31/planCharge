package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance;

/**
 * Created by frederic.danna on 22/04/2017.
 */
public class ImportanceNonUniqueException extends ImportanceDaoException {

    public ImportanceNonUniqueException(String codeImportance) {
        super("Importance en N exemplaires : '" + codeImportance + "'.");
    }

}
