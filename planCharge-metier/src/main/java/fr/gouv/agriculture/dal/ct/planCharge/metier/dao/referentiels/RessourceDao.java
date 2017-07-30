package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.metier.dao.DataAcessObject;
import fr.gouv.agriculture.dal.ct.metier.dao.EntityNotFoundException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.RessourceDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceGenerique;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Ressource;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceHumaine;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class RessourceDao<R extends Ressource<R>> implements DataAcessObject<RessourceHumaine, String> {


    private static RessourceDao instance;

    public static RessourceDao instance() {
        if (instance == null) {
            instance = new RessourceDao();
        }
        return instance;
    }


    @NotNull
//    @Autowired
    private static RessourceHumaineDao ressourceHumaineDao = RessourceHumaineDao.instance();


    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private RessourceDao() {
        super();
    }


    @NotNull
    public Ressource loadAny(@NotNull String codeRessource) throws EntityNotFoundException {

        RessourceGenerique ressourceGenerique = RessourceGenerique.valeurOuNull(codeRessource);
        if (ressourceGenerique != null) {
            return ressourceGenerique;
        }

        return ressourceHumaineDao.load(codeRessource);
    }


    @NotNull
    public List<Ressource> listAll() throws DaoException {
        List<Ressource> ressources = Arrays.asList(RessourceGenerique.RESSOURCES_GENERIQUES);
        ressources.addAll(ressourceHumaineDao.list());
        return ressources;
    }
}
