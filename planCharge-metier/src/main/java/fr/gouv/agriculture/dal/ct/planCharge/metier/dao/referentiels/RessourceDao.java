package fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.metier.dao.DataAcessObject;
import fr.gouv.agriculture.dal.ct.metier.dao.EntityNotFoundException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Ressource;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceGenerique;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.RessourceHumaine;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * Created by frederic.danna on 26/03/2017.
 */
@SuppressWarnings("ClassHasNoToStringMethod")
public /*abstract*/ class RessourceDao<R extends Ressource<R>> /*extends AbstractDao*/ implements DataAcessObject<RessourceHumaine, String> {


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
    public Ressource<?> load(@NotNull String codeRessource) throws EntityNotFoundException {

        RessourceGenerique ressourceGenerique = RessourceGenerique.valeurOuNull(codeRessource);
        if (ressourceGenerique != null) {
            return ressourceGenerique;
        }

        return ressourceHumaineDao.load(codeRessource);
    }

//    @Override
    @NotNull
    public Ressource createOrUpdate(@NotNull Ressource ressource) throws DaoException {

        RessourceGenerique ressourceGenerique = RessourceGenerique.valeurOuNull(ressource.getCode());
        if (ressourceGenerique != null) {
            return ressourceGenerique;
        }

        RessourceHumaine ressourceHumaine = (RessourceHumaine) ressource;
        return ressourceHumaineDao.createOrUpdate(ressourceHumaine);
    }

    @SuppressWarnings("rawtypes")
    @NotNull
    public List<Ressource> listAll() throws DaoException {
        List<Ressource> ressources = Arrays.asList(RessourceGenerique.RESSOURCES_GENERIQUES);
        ressources.addAll(ressourceHumaineDao.list());
        return ressources;
    }
}
