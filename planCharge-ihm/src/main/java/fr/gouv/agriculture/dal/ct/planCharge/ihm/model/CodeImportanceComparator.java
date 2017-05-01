package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.Comparator;

/**
 * Created by frederic.danna on 22/04/2017.
 */
public class CodeImportanceComparator implements Comparator<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CodeImportanceComparator.class);

    //    @Autowired
    @NotNull
    private ImportanceDao importanceDao = ImportanceDao.instance();

    @Override
    public int compare(@NotNull String codeImportance1, @NotNull String codeImportance2) {
        try {
            /*@NotNull*/ Importance i1 = importanceDao.load(codeImportance1);
            /*@NotNull*/ Importance i2 = importanceDao.load(codeImportance2);
            return i1.compareTo(i2);
        } catch (DaoException e) {
            LOGGER.error("Impossible de trier les importances.", e);
            return 0;
        }
    }
}
