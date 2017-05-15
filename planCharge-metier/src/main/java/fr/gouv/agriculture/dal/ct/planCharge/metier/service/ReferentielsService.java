package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProfilDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.ProjetAppliDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.RessourceDao;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ReferentielsService.class);

    private static class InstanceHolder {
        private static final ReferentielsService INSTANCE = new ReferentielsService();
    }

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
    private RessourceDao ressourceDao = RessourceDao.instance();
    //    @Autowired
    @NotNull
    private ProfilDao profilDao = ProfilDao.instance();


    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private ReferentielsService() {
        super();
    }


    public List<CategorieTache> categoriesTache() throws ServiceException {
        CategorieTache[] categories = CategorieTache.values();
/*
        if (categories == null) {
            throw new ServiceException("Impossible de lister les catégories de tâche.");
        }
*/
        return Arrays.asList(categories);
    }

    public List<SousCategorieTache> sousCategoriesTache() throws ServiceException {
        SousCategorieTache[] sousCategories = SousCategorieTache.values();
/*
        if (sousCategories == null) {
            throw new ServiceException("Impossible de lister les sous-catégories de tâche.");
        }
*/
        return Arrays.asList(sousCategories);
    }

    public List<Importance> importances() throws ServiceException {
        try {
            return importanceDao.list();
        } catch (DaoException e) {
            throw new ServiceException("Impossible de lister les importances.", e);
        }
    }

    public List<ProjetAppli> projetsApplis() throws ServiceException {
        try {
            List<ProjetAppli> projetApplis = projetAppliDao.list();
            return projetApplis;
        } catch (DaoException e) {
            //noinspection HardcodedFileSeparator
            throw new ServiceException("Impossible de lister les projets/applis.", e);
        }
    }

    public List<Ressource> ressources() throws ServiceException {
        try {
            return ressourceDao.list();
        } catch (DaoException e) {
            throw new ServiceException("Impossible de lister les ressources.", e);
        }
    }

    public List<Profil> profils() throws ServiceException {
        try {
            return profilDao.list();
        } catch (DaoException e) {
            throw new ServiceException("Impossible de lister les profils.", e);
        }
    }
}
