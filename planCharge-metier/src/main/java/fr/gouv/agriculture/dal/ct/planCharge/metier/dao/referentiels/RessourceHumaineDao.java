package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels;

import fr.gouv.agriculture.dal.ct.metier.dao.AbstractDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceHumaine;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class RessourceHumaineDao extends /*RessourceDao*/ AbstractDao<RessourceHumaine, String> {

    private static final Map<String, RessourceHumaine> CACHE = new HashMap<>();

    private static RessourceHumaineDao instance;

    public static RessourceHumaineDao instance() {
        if (instance == null) {
            instance = new RessourceHumaineDao();
        }
        return instance;
    }


    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private RessourceHumaineDao() {
        super();
    }


//    @Override
    @NotNull
    protected Map<String, RessourceHumaine> getCache() {
        return CACHE;
    }

}
