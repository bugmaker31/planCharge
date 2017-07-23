package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DataAcessObject;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.EntityNotFoundException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceGenerique;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Ressource;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceHumaine;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class RessourceDao implements DataAcessObject<RessourceHumaine, String> {


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
                codeRessource.equals(RessourceGenerique.NIMPORTE_QUI.getTrigramme()) ? RessourceGenerique.NIMPORTE_QUI :
                        (codeRessource.equals(RessourceGenerique.TOUS.getTrigramme()) ? RessourceGenerique.TOUS :
                                ressourceHumaineDao.load(codeRessource))
        );
        return ressource;
    }
}
