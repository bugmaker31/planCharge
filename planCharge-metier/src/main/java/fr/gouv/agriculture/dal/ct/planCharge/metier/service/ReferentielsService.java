package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProfilDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProjetAppliDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.RessourceHumaineDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.StatutDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * Created by frederic.danna on 14/05/2017.
 *
 * @author frederic.danna
 */
public class ReferentielsService {

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(ReferentielsService.class);

    private static class InstanceHolder {
        private static final ReferentielsService INSTANCE = new ReferentielsService();
    }

    @NotNull
    public static ReferentielsService instance() {
        return InstanceHolder.INSTANCE;
    }

    @NotNull
    private ImportanceDao importanceDao = ImportanceDao.instance();
    //    @Autowired
    @NotNull
    private ProjetAppliDao projetAppliDao = ProjetAppliDao.instance();
    //    @Autowired
    @NotNull
    private StatutDao statutDao = StatutDao.instance();
    //    @Autowired
    @NotNull
    private RessourceHumaineDao ressourceHumaineDao = RessourceHumaineDao.instance();
    //    @Autowired
    @NotNull
    private ProfilDao profilDao = ProfilDao.instance();


    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private ReferentielsService() {
        super();
    }


    @NotNull
    public List<CategorieTache> categoriesTache() throws ServiceException {
        CategorieTache[] categories = CategorieTache.values();
/*
        if (categories == null) {
            throw new ServiceException("Impossible de lister les catégories de tâche.");
        }
*/
        return Arrays.asList(categories);
    }

    @NotNull
    public List<SousCategorieTache> sousCategoriesTache() throws ServiceException {
        SousCategorieTache[] sousCategories = SousCategorieTache.values();
/*
        if (sousCategories == null) {
            throw new ServiceException("Impossible de lister les sous-catégories de tâche.");
        }
*/
        return Arrays.asList(sousCategories);
    }

    @NotNull
    public List<Importance> importances() throws ServiceException {
        try {
            return importanceDao.list();
        } catch (DaoException e) {
            throw new ServiceException("Impossible de lister les importances.", e);
        }
    }

    @NotNull
    public List<ProjetAppli> projetsApplis() throws ServiceException {
        try {
            List<ProjetAppli> projetApplis = projetAppliDao.list();
            return projetApplis;
        } catch (DaoException e) {
            //noinspection HardcodedFileSeparator
            throw new ServiceException("Impossible de lister les projets/applis.", e);
        }
    }

    @NotNull
    public List<Statut> statuts() throws ServiceException {
        try {
            List<Statut> statuts = statutDao.list();
            return statuts;
        } catch (DaoException e) {
            throw new ServiceException("Impossible de lister les statuts.", e);
        }
    }

    @NotNull
    public List<RessourceHumaine> ressources() throws ServiceException {
        try {
            return ressourceHumaineDao.list();
        } catch (DaoException e) {
            throw new ServiceException("Impossible de lister les ressources.", e);
        }
    }

    @NotNull
    public List<Profil> profils() throws ServiceException {
        try {
            return profilDao.list();
        } catch (DaoException e) {
            throw new ServiceException("Impossible de lister les profils.", e);
        }
    }
}
