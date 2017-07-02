package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.AbstractDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DataAcessObject;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.EntityNotFoundException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.PseudoRessource;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Ressource;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceHumaine;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class RessourceDao  implements DataAcessObject<RessourceHumaine,String> {


    private static RessourceDao instance;

    public static RessourceDao instance() {
        if (instance == null) {
            instance = new RessourceDao();
        }
        return instance;
    }


    @NotNull
//    @Autowired
    private RessourceHumaineDao ressourceHumaineDao = RessourceHumaineDao.instance();


    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private RessourceDao() {
        super();
    }


    @NotNull
    public Ressource load(@NotNull String codeRessource) throws EntityNotFoundException {
        Ressource ressource = (
                codeRessource.equals(PseudoRessource.NIMPORTE_QUI.getTrigramme()) ? PseudoRessource.NIMPORTE_QUI :
                        (codeRessource.equals(PseudoRessource.TOUS.getTrigramme()) ? PseudoRessource.TOUS :
                                ressourceHumaineDao.load(codeRessource))
        );
        return ressource;
    }
}
