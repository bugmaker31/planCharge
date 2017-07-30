package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceInexistanteException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.referentiels.importance.ImportanceNonUniqueException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.*;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private RessourceDao ressourceDao = RessourceDao.instance();
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
    public List<CategorieTacheDTO> categoriesTache() throws ServiceException {
        return CategorieTacheDTO.CATEGORIES;
    }

    @NotNull
    public List<SousCategorieTacheDTO> sousCategoriesTache() throws ServiceException {
        return Arrays.asList(SousCategorieTacheDTO.values());
    }


    @NotNull
    public List<ImportanceDTO> importances() throws ServiceException {
        try {
            List<Importance> importances = importanceDao.list();
            return importances.stream().map(ImportanceDTO::from).collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException("Impossible de lister les importances.", e);
        }
    }

    @NotNull
    public ImportanceDTO importance(@NotNull String codeImportance) throws ServiceException {
        try {
            Importance importance = importanceDao.loadByCode(codeImportance);
            return ImportanceDTO.from(importance);
        } catch (ImportanceInexistanteException | ImportanceNonUniqueException e) {
            throw new ServiceException("Impossible de retrouver l'importance '" + codeImportance + "'.", e);
        }
    }


    @NotNull
    public List<ProjetAppliDTO> projetsApplis() throws ServiceException {
        try {
            List<ProjetAppli> projetsApplis = projetAppliDao.list();
            return projetsApplis.stream().map(ProjetAppliDTO::from).collect(Collectors.toList());
        } catch (DaoException e) {
            //noinspection HardcodedFileSeparator
            throw new ServiceException("Impossible de lister les projets/applis.", e);
        }
    }

    @NotNull
    public ProjetAppliDTO projetAppli(@NotNull String codeProjetAppli) throws ServiceException {
        try {
            ProjetAppli projetAppli = projetAppliDao.load(codeProjetAppli);
            return ProjetAppliDTO.from(projetAppli);
        } catch (DaoException e) {
            //noinspection HardcodedFileSeparator
            throw new ServiceException("Impossible de retrouver le projet/appli '" + codeProjetAppli + "'.", e);
        }
    }


    @NotNull
    public List<StatutDTO> statuts() throws ServiceException {
        try {
            List<Statut> statuts = statutDao.list();
            return statuts.stream().map(StatutDTO::from).collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException("Impossible de lister les statuts.", e);
        }
    }

    @NotNull
    public StatutDTO statut(@NotNull String codeStatut) throws ServiceException {
        try {
            Statut statut = statutDao.load(codeStatut);
            return StatutDTO.from(statut);
        } catch (DaoException e) {
            //noinspection HardcodedFileSeparator
            throw new ServiceException("Impossible de retrouver le statut '" + codeStatut + "'.", e);
        }
    }


    @NotNull
    public List<RessourceDTO> ressources() throws ServiceException {
        try {
            List<Ressource> ressources = Arrays.asList(RessourceGenerique.RESSOURCES_GENERIQUES);
            ressources.addAll(ressourceDao.listAll());
            return ressources.stream().map(RessourceDTO::from).collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException("Impossible de lister les ressources.", e);
        }
    }

    @NotNull
    public RessourceDTO ressource(@NotNull String codeRessource) throws ServiceException {
        try {
            Ressource ressource = ressourceDao.loadAny(codeRessource);
            return RessourceDTO.from(ressource);
        } catch (DaoException e) {
            throw new ServiceException("Impossible de retrouver la ressource '" + codeRessource + "'.", e);
        }
    }

    @NotNull
    public List<RessourceHumaineDTO> ressourcesHumaines() throws ServiceException {
        try {
            List<RessourceHumaine> ressourcesHumaines = ressourceHumaineDao.list();
            return ressourcesHumaines.stream().map(RessourceHumaineDTO::from).collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException("Impossible de lister les ressources.", e);
        }
    }


    @NotNull
    public List<ProfilDTO> profils() throws ServiceException {
        try {
            List<Profil> profils = profilDao.list();
            return profils.stream().map(ProfilDTO::from).collect(Collectors.toList());
        } catch (DaoException e) {
            throw new ServiceException("Impossible de lister les profils.", e);
        }
    }

    @NotNull
    public ProfilDTO profil(@NotNull String codeProfil) throws ServiceException {
        try {
            Profil profil = profilDao.load(codeProfil);
            return ProfilDTO.from(profil);
        } catch (DaoException e) {
            throw new ServiceException("Impossible de retrouver le profil '" + codeProfil + "'.", e);
        }
    }
}
