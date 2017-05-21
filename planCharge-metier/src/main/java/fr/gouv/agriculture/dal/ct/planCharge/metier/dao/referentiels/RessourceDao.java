package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.AbstractDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.MockedDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Ressource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class RessourceDao extends MockedDao<Ressource, String> {

    private static final Map<String, Ressource> CACHE = new HashMap<>();

    private static RessourceDao instance;

    public static RessourceDao instance() {
        if (instance == null) {
            instance = new RessourceDao();
        }
        return instance;
    }

    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private RessourceDao() {
        super();
    }

    @Override
    protected Map<String, Ressource> getCache() {
        return CACHE;
    }

    @Override
    protected Ressource newEntity(String id) {
        return new Ressource(id);
    }
}
