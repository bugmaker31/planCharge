package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels;

import fr.gouv.agriculture.dal.ct.metier.dao.AbstractDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.ProjetAppli;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class ProjetAppliDao extends AbstractDao<ProjetAppli,String> {

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjetAppliDao.class);

    @NotNull
    private static final Map<String, ProjetAppli> CACHE = new HashMap<>();

    private static ProjetAppliDao instance = null;

    @NotNull
    public static ProjetAppliDao instance() {
        if (instance == null) {
            instance = new ProjetAppliDao();
        }
        return instance;
    }

    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private ProjetAppliDao() {
        super();
    }

    @Override
    @NotNull
    protected Map<String, ProjetAppli> getCache() {
        return CACHE;
    }

}
