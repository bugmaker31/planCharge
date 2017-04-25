package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.AbstractDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.ProjetAppli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class ProjetAppliDao extends AbstractDao<ProjetAppli, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjetAppliDao.class);

    private static final Map<String, ProjetAppli> CACHE = new HashMap();

    @Override
    protected Map<String, ProjetAppli> getCache() {
        return CACHE;
    }

    @Override
    protected ProjetAppli newEntity(String id) {
        return new ProjetAppli(id);
    }
}
