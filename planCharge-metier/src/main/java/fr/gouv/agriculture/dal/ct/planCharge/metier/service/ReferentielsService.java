package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by frederic.danna on 14/05/2017.
 *
 * @author frederic.danna
 */
public class ReferentielsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReferentielsService.class);

    private static ReferentielsService instance;

    public static ReferentielsService instance() {
        if (instance == null) {
            instance = new ReferentielsService();
        }
        return instance;
    }

    //    @Autowired
    @NotNull
    private ImportanceDao importanceDao = ImportanceDao.instance();


    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private ReferentielsService() {
        super();
    }


    public List<Importance> importances() throws ServiceException {
        try {
            List<Importance> importances = importanceDao.list();
            return importances;
        } catch (DaoException e) {
            throw new ServiceException("Impossible de lister les importances.", e);
        }
    }
}
