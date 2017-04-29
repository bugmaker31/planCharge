package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.AbstractDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.EntityNotFoundException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class ImportanceDao extends AbstractDao<Importance, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportanceDao.class);

    private static final Map<String, Importance> CACHE = new HashMap<>();

    @Override
    protected Map<String, Importance> getCache() {
        return CACHE;
    }

    @Override
    protected Importance newEntity(String id) throws ImportanceDaoException {
        try {
            return new Importance(id);
        } catch (ModeleException e) {
            throw new ImportanceDaoException("Impossible d'instancier l'importance '" + id + "'.", e);
        }
    }

    public Importance loadByCode(String code) throws ImportanceDaoException, ImportanceInexistanteException, ImportanceNonUniqueException, EntityNotFoundException, DaoException {
        String id = code; // TODO FDA 2017/04 Coder
        return load(id);
    }
}
