package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels;

import fr.gouv.agriculture.dal.ct.metier.dao.AbstractDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Statut;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class StatutDao extends AbstractDao<Statut,String> {

    private static final Map<String, Statut> CACHE = new HashMap<>();

    private static StatutDao instance;

    public static StatutDao instance() {
        if (instance == null) {
            instance = new StatutDao();
        }
        return instance;
    }

    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private StatutDao() {
        super();
    }

    @NotNull
    @Override
    protected Map<String, Statut> getCache() {
        return CACHE;
    }

}
