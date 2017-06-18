package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import com.sun.deploy.util.OrderedHashSet;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.tache.TacheDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.TacheSansPlanificationException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    /**
     * @param planCharge Le plan de charge actuel, pour une certaine date d'état, et la planification qui va avec.
     * @param dateEtat   La (nouvelle) date d'état de la planification.
     * @return La (nouvelle) planification pour la (nouvelle) date d'état fournie.
     */
    @NotNull
    public Planifications planification(@NotNull PlanCharge planCharge, @NotNull LocalDate dateEtat) {

/*
        // Si la date d'état n'a pas changé, la planification non plus :
        if (dateEtat.equals(planCharge.getDateEtat())) {
            return planCharge.getPlanifications();
        }
*/

        // Si la date d'état a changé, la planification change forcément aussi : il faut ajouter ou retirer des périodes de planification,
        // et initialiser les charges de chaque tâche en cas d'ajout :
        final LocalDate debutPlanif = dateEtat;
        final LocalDate finPlanif = dateEtat.plusDays(Planifications.NBR_SEMAINES_PLANIFIEES * 7); // FIXME FDA 2017/07  Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
        Planifications planifications = planCharge.getPlanifications();
        planifications.keySet().parallelStream()
                .forEach(tache -> {
                    Map<LocalDate, Double> planifTache = planCharge.getPlanifications().get(tache);

                    // On supprime les périodes qui sont en dehors du calendrier de planification :
                    Set<LocalDate> periodesASupprimer = new TreeSet<>(); // Un TreeSet pour garder le tri (par date), juste pour faciliter le débogage.
                    for (LocalDate debutPeriodeTache : planifTache.keySet()) {
                        if (debutPeriodeTache.isBefore(debutPlanif) || debutPeriodeTache.isAfter(finPlanif)) {
                            periodesASupprimer.add(debutPeriodeTache);
                        }
                    }
                    periodesASupprimer.forEach(debutPeriodeTache -> {
                        planifTache.remove(debutPeriodeTache);
                        LOGGER.debug("Période commençant le {} supprimée pour la tâche {}.", debutPeriodeTache, tache.noTache());
                    });

                    // On ajoute les planifications qui manquent au calendrier :
                    for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
                        LocalDate debutPeriodeTache = dateEtat.plusDays((noSemaine - 1) * 7);// FIXME FDA 2017/07  Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                        if (!planifTache.containsKey(debutPeriodeTache)) {
                            double chargeTachePeriode = nouvelleCharge(tache, debutPeriodeTache);
                            planifTache.put(debutPeriodeTache, chargeTachePeriode);
                            LOGGER.debug("Période commençant le {} ajoutée pour la tâche {} (chargée à {}).", debutPeriodeTache, tache.noTache(), chargeTachePeriode);
                        }
                    }
                });

        return planifications;
    }

    private double nouvelleCharge(@NotNull Tache tache, @NotNull LocalDate debutPeriodeTache) {
        return 0.0; // FIXME FDA 2017/07 Coder (si la tâche est une provision, retourner la charge pour la période, sinon retourner zéro.
    }
}
