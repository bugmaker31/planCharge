package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.AbstractDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class ImportanceDao extends AbstractDao<Importance, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportanceDao.class);

    private static final Map<String, Importance> CACHE = new HashMap();

    @Override
    protected Map<String, Importance> getCache() {
        return CACHE;
    }

    @Override
    protected Importance newEntity(String id) {
        return new Importance(id);
    }

    public Importance loadByCode(String code) throws ImportanceInexistanteException, ImportanceNonUniqueException {

        List<Importance> importances = CACHE.values().stream().filter(importance -> importance.getCode().equals(code)).collect(Collectors.toList());

        if (importances.size() == 0) {
            throw new ImportanceInexistanteException(code);
        }
        if (importances.size() >= 2) {
            throw new ImportanceNonUniqueException(code);
        }
        assert importances.size() == 1;

        return importances.get(0);
    }
}
