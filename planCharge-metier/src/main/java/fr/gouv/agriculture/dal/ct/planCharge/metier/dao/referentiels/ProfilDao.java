package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels;

import fr.gouv.agriculture.dal.ct.metier.dao.AbstractDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Profil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class ProfilDao extends AbstractDao<Profil,String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfilDao.class);

    private static final Map<String, Profil> CACHE = new HashMap<>();

    private static ProfilDao instance;

    public static ProfilDao instance() {
        if (instance == null) {
            instance = new ProfilDao();
        }
        return instance;
    }

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public ProfilDao() {
        super();
    }

    @Override
    protected Map<String, Profil> getCache() {
        return CACHE;
    }

}
