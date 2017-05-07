package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.tache.TacheDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.TacheSansPlanificationException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class PlanChargeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlanChargeService.class);

    private static PlanChargeService instance;

    public static PlanChargeService instance() {
        if (instance == null) {
            instance = new PlanChargeService();
        }
        return instance;
    }

    //    @Autowired
    @NotNull
    private PlanChargeDao planChargeDao = PlanChargeDao.instance();

    //    @Autowired
    @NotNull
    private TacheDao tacheDao = TacheDao.instance();


    // 'private' pour empêcher quiconque d'autre d'instancier cette classe (pattern "Factory").
    private PlanChargeService() {
        super();
    }

    @NotNull
    public PlanCharge charger(@NotNull LocalDate dateEtat) throws ServiceException {
        try {
            return planChargeDao.load(dateEtat);
        } catch (DaoException e) {
            throw new ServiceException(
                    "Impossible de charger le plan de charge en date du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".",
                    e);
        }
    }

    @NotNull
    public PlanCharge charger(@NotNull File ficPlanCharge) throws ServiceException {
        try {
            return planChargeDao.load(ficPlanCharge);
        } catch (DaoException e) {
            throw new ServiceException(
                    "Impossible de charger le plan de charge depuis le fichier '" + ficPlanCharge.getAbsolutePath() + "'.",
                    e);
        }
    }


    public void sauver(@NotNull PlanCharge planCharge) throws ServiceException {
        LocalDate dateEtat = planCharge.getDateEtat();
        try {
            planChargeDao.sauver(planCharge);
        } catch (DaoException e) {
            throw new ServiceException(
                    "Impossible de sauver le plan de charge en date du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".",
                    e);
        }
    }

    @NotNull
    public RapportMajTaches majTachesDepuisCalc(@NotNull PlanCharge planCharge, @NotNull File ficCalcTaches) throws ServiceException {
        RapportMajTaches rapport;
        try {

            Set<Tache> tachesImportees = tacheDao.importerDepuisCalc(ficCalcTaches);

            rapport = new RapportMajTaches(planCharge.getPlanifications().size(), tachesImportees.size());

            // MàJ du plan de charge :
            // - suppression des tâches qui n'existent plus (terminée/annulée/etc.)
            // - ajout des tâches qui ont été créées depuis
            // - mise à jour des tâches qui existaient déjà avant
            for (Tache tacheImportee : tachesImportees) {

                if (!planCharge.getPlanifications().taches().contains(tacheImportee)) {

                    // Ajout des tâches qui ont été créées depuis :
                    planCharge.getPlanifications().ajouter(tacheImportee, planCharge.getDateEtat());
                    LOGGER.debug("Tâche " + tacheImportee + " ajoutée.");
                    rapport.incrNbrTachesAjoutees();
                } else {
                    assert planCharge.getPlanifications().taches().contains(tacheImportee);

                    // Mise à jour des tâches qui existaient déjà avant, en gardant la planification actuelle :
                    Tache tacheActuelle = planCharge.getPlanifications().tache(tacheImportee.getId());
                    Map<LocalDate, Double> calendrierTache = planCharge.getPlanifications().calendrier(tacheActuelle);
                    planCharge.getPlanifications().put(tacheImportee, calendrierTache);
                    LOGGER.debug("Tâche " + tacheActuelle + " màj.");
                    rapport.incrNbrTachesMisesAJour();
                }
            }
            // Suppression des tâches qui n'existent plus (terminée/annulée/etc.) :
            Set<Tache> tachesActuellesASupprimer = new HashSet<>();
            for (Tache tacheActuelle : planCharge.getPlanifications().taches()) {
                if (!tachesImportees.contains(tacheActuelle)) {
                    tachesActuellesASupprimer.add(tacheActuelle);
                } else {
                    // Tâche déjà mise à jour, dans la boucle 'for' ci-dessus.
                }
            }
            tachesActuellesASupprimer.forEach(tacheActuelle -> {
                planCharge.getPlanifications().remove(tacheActuelle);
                rapport.incrNbrTachesSupprimees();
                LOGGER.debug("Tâche " + tacheActuelle + " supprimée.");
            });

        } catch (DaoException | TacheSansPlanificationException e) {
            throw new ServiceException(
                    "Impossible de màj les tâches depuis le fichier Calc '" + ficCalcTaches.getAbsolutePath() + "'.",
                    e);
        }
        return rapport;
    }

    @NotNull
    public PlanCharge importerDepuisCalc(@NotNull File ficCalc) throws ServiceException {
        try {
            return planChargeDao.importerDepuisCalc(ficCalc);
        } catch (DaoException e) {
            throw new ServiceException(
                    "Impossible d'importer le plan de charge depuis le fichier Calc '" + ficCalc.getAbsolutePath() + "'.",
                    e);
        }
    }

    public File fichierPersistancePlanCharge(@NotNull LocalDate dateEtat) throws ServiceException {
        try {
            return planChargeDao.fichierPlanCharge(dateEtat);
        } catch (PlanChargeDaoException e) {
            throw new ServiceException(
                    "Impossible de déterminer le nom du fichier de persistance du plan de charge, pour la date du "
                            + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".",
                    e);
        }
    }
}
