package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.Comparator;

/**
 * Created by frederic.danna on 22/04/2017.
 * @author frederic.danna
 */
public class CodeImportanceComparator implements Comparator<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CodeImportanceComparator.class);

    public static final Comparator<String> COMPARATEUR = new CodeImportanceComparator();

    //    @Autowired
    @NotNull
    private ImportanceDao importanceDao = ImportanceDao.instance();

    @Override
    public int compare(@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter") @NotNull String codeImportance1, @SuppressWarnings("ParameterNameDiffersFromOverriddenParameter") @NotNull String codeImportance2) {
        try {
            Importance i1 = importanceDao.loadByCode(codeImportance1);
            Importance i2 = importanceDao.loadByCode(codeImportance2);
            assert i1 != null;
            assert i2 != null;
            return i1.compareTo(i2);
        } catch (DaoException e) {
            LOGGER.error("Impossible de trier les importances '" + codeImportance1 + "' et '" + codeImportance2 + "'.", e);
            return 0;
        }
    }

}
