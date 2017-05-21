package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.AbstractDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.EntityNotFoundException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.MockedDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class ImportanceDao extends MockedDao<Importance, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportanceDao.class);

    private static final Map<String, Importance> CACHE = new HashMap<>();

    private static ImportanceDao instance;

    public static ImportanceDao instance() {
        if (instance == null) {
            instance = new ImportanceDao();
        }
        return instance;
    }

    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private ImportanceDao() {
        super();
    }

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
        List<Importance> importances = findByCode(code);
        if (importances.size() == 0) {
            throw new ImportanceInexistanteException(code);
        }
        if (importances.size() >= 2) {
            throw new ImportanceNonUniqueException(code);
        }
        Importance importance = importances.get(0);
        return importance;
    }

    @NotNull
    private List<Importance> findByCode(String code) {
        List<Importance> found;

        // TODO FDA 2017/03 Débouchonner : retrouver depuis la couche de persistence

        found = CACHE.keySet().stream()
                .filter(codeImportance -> CACHE.containsKey(codeImportance))
                .filter(codeImportance -> CACHE.get(codeImportance).getCode().equals(code))
                .map(codeImportance  -> CACHE.get(codeImportance))
                .collect(Collectors.toList());

        return found;
    }

}
