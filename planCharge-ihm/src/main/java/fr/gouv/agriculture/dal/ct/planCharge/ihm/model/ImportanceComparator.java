package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceDaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.Comparator;

/**
 * Created by frederic.danna on 22/04/2017.
 */
public class ImportanceComparator implements Comparator<String> {

    @Autowired
    private ImportanceDao importanceDao;

    @Override
    public int compare(@NotNull String codeImportance1, @NotNull String codeImportance2) throws IhmException {
        try {
            /*@NotNull*/ Importance i1 = importanceDao.loadByCode(codeImportance1);
            /*@NotNull*/ Importance i2 = importanceDao.loadByCode(codeImportance2);
            return i1.compareTo(i2);
        } catch (ImportanceDaoException e) {
            throw new IhmException("Impossible de trier les importances.", e);
        }
    }
}
