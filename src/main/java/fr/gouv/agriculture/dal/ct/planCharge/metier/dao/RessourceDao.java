package fr.gouv.agriculture.dal.ct.planCharge.metier.dao;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.Ressource;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class RessourceDao extends AbstractDao<Ressource, String> {

    private static final Map<String, Ressource> CACHE = new HashMap();

    @Override
    protected Map<String, Ressource> getCache() {
        return CACHE;
    }

    @Override
    protected Ressource newEntity(String id) {
        return new Ressource(id);
    }
}
