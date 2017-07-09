package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.AbstractDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.JourFerie;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Profil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class JourFerieDao extends AbstractDao<JourFerie,LocalDate> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JourFerieDao.class);

    private static final Map<LocalDate, JourFerie> CACHE = new HashMap<>();

    private static JourFerieDao instance;


    public static JourFerieDao instance() {
        if (instance == null) {
            instance = new JourFerieDao();
        }
        return instance;
    }

    /**
     * Constructeur vide (appelé notamment par JAXB).
     *
     * @return
     */
    public JourFerieDao() {
        super();
    }


    @Override
    protected Map<LocalDate, JourFerie> getCache() {
        return CACHE;
    }

}
