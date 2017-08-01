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
 *
 * @author frederic.danna
 */
public class ImportanceComparator implements Comparator<ImportanceBean> {

    public static final Comparator<ImportanceBean> COMPARATEUR = new ImportanceComparator();
    private static final Logger LOGGER = LoggerFactory.getLogger(ImportanceComparator.class);

    //    @Autowired
    @NotNull
    private ImportanceDao importanceDao = ImportanceDao.instance();

    @Override
    public int compare(@SuppressWarnings("ParameterNameDiffersFromOverriddenParameter") @NotNull ImportanceBean i1, @SuppressWarnings("ParameterNameDiffersFromOverriddenParameter") @NotNull ImportanceBean i2) {
        return new Integer(i1.getOrdre()).compareTo(new Integer(i2.getOrdre()));
    }

}
