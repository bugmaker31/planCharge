package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;

import javax.validation.constraints.NotNull;
import java.util.Comparator;

/**
 * Created by frederic.danna on 22/04/2017.
 */
public class CodeImportanceComparator implements Comparator<String> {

    //    @Autowired
    @NotNull
    private ImportanceDao importanceDao = ImportanceDao.instance();

    @Override
    public int compare(@NotNull String codeImportance1, @NotNull String codeImportance2) throws IhmException {
        try {
            /*@NotNull*/ Importance i1 = importanceDao.load(codeImportance1);
            /*@NotNull*/ Importance i2 = importanceDao.load(codeImportance2);
            return i1.compareTo(i2);
        } catch (DaoException e) {
            throw new IhmException("Impossible de trier les importances.", e);
        }
    }
}
