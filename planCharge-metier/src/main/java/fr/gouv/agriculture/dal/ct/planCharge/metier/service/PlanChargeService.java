package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import fr.gouv.agriculture.dal.ct.metier.MetierException;
import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.ControleurRegles;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.ViolationRegleGestion;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.ViolationsReglesGestionException;
import fr.gouv.agriculture.dal.ct.metier.service.AbstractService;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.tache.TacheDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanChargeDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanificationsDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.TacheSansPlanificationException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class PlanChargeService extends AbstractService {

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
    public PlanCharge charger(@NotNull LocalDate dateEtat, @NotNull RapportChargementPlanCharge rapport) throws ServiceException {
        try {
            return planChargeDao.charger(dateEtat, rapport);
        } catch (DaoException e) {
            throw new ServiceException(
                    "Impossible de charger le plan de charge en date du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".",
                    e);
        }
    }

    @NotNull
    public PlanChargeDTO charger(@NotNull File ficPlanCharge, @NotNull RapportChargementPlanCharge rapport) throws ServiceException {
        try {
            PlanCharge planCharge = planChargeDao.charger(ficPlanCharge, rapport);
            return PlanChargeDTO.from(planCharge);
        } catch (DTOException | DaoException e) {
            throw new ServiceException(
                    "Impossible de charger le plan de charge depuis le fichier '" + ficPlanCharge.getAbsolutePath() + "'.",
                    e);
        }
    }

    // RG_Gal_ControlerRegles Contrôler les règles https://github.com/bugmaker31/planCharge/wiki/R%C3%A8gles-de-gestion-%3A-Globales#rg_gal_controlerregles-contr%C3%B4ler-les-r%C3%A8gles
    public void controlerReglesGestion(@NotNull PlanChargeDTO planChargeDTO) throws MetierException, ViolationsReglesGestionException {
        List<ViolationRegleGestion> violationsRegles = ControleurRegles.violations(planChargeDTO);
        if (!violationsRegles.isEmpty()) {
            throw new ViolationsReglesGestionException("Règle(s) de gestion non respectée(s)", violationsRegles);
        }
    }

    public void sauver(@NotNull PlanChargeDTO planChargeDTO, @NotNull RapportSauvegarde rapport) throws ServiceException, ViolationsReglesGestionException {
        LocalDate dateEtat = planChargeDTO.getDateEtat();
        try {

            // RG_Gal_ControlerRegles Contrôler les règles https://github.com/bugmaker31/planCharge/wiki/R%C3%A8gles-de-gestion-%3A-Globales#rg_gal_controlerregles-contr%C3%B4ler-les-r%C3%A8gles
            rapport.setAvancement("Contrôle des règles de gestion...");
            controlerReglesGestion(planChargeDTO);

            rapport.setAvancement("Sauvegarde...");
            PlanCharge planCharge = planChargeDTO.toEntity();
            planChargeDao.sauver(planCharge, rapport);

        } catch (MetierException e) {
            throw new ServiceException(
                    "Impossible de sauver le plan de charge en date du " + dateEtat.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + ".",
                    e);
        }
    }


    @NotNull
    public RapportImportTaches majTachesDepuisCalc(@NotNull PlanChargeDTO planCharge, @NotNull File ficCalcTaches, @NotNull final RapportImportTaches rapport) throws ServiceException {
        try {

            Set<Tache> tachesImportees = tacheDao.importerDepuisCalc(ficCalcTaches, rapport);

//            rapport = new RapportImportTaches(planCharge.getPlanifications().size(), tachesImportees.size());

            // MàJ du plan de charge :
            // - suppression des tâches qui n'existent plus (terminée/annulée/etc.)
            // - ajout des tâches qui ont été créées depuis
            // - mise à jour des tâches qui existaient déjà avant
            for (Tache tacheImportee : tachesImportees) {
                TacheDTO tacheImporteeDTO = TacheDTO.from(tacheImportee);

                assert planCharge.getPlanifications() != null;
                if (!planCharge.getPlanifications().taches().contains(tacheImporteeDTO)) {

                    // Ajout des tâches qui ont été créées depuis :
                    assert planCharge.getDateEtat() != null;
                    planCharge.getPlanifications().ajouter(tacheImporteeDTO, planCharge.getDateEtat());
                    rapport.incrNbrTachesAjoutees();
                    LOGGER.debug("Tâche " + tacheImportee + " ajoutée.");
                } else {
                    assert planCharge.getPlanifications().taches().contains(tacheImporteeDTO);

                    // Mise à jour des tâches qui existaient déjà avant, en gardant la planification actuelle :
                    TacheDTO tacheActuelleDTO = planCharge.getPlanifications().tache(tacheImportee.getId());
                    assert tacheActuelleDTO != null;
                    Map<LocalDate, Double> calendrierTache = planCharge.getPlanifications().calendrier(tacheActuelleDTO);
                    planCharge.getPlanifications().put(tacheImporteeDTO, calendrierTache);
                    rapport.incrNbrTachesMisesAJour();
                    LOGGER.debug("Tâche " + tacheActuelleDTO + " màj.");
                }
            }
            // Suppression des tâches qui n'existent plus (terminée/annulée/etc.) :
            Set<TacheDTO> tachesActuellesASupprimer = new HashSet<>();
            assert planCharge.getPlanifications() != null;
            for (TacheDTO tacheActuelle : planCharge.getPlanifications().taches()) {
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

        } catch (DTOException | DaoException | TacheSansPlanificationException e) {
            throw new ServiceException(
                    "Impossible de màj les tâches depuis le fichier Calc '" + ficCalcTaches.getAbsolutePath() + "'.",
                    e);
        }
        return rapport;
    }

    @NotNull
    public PlanChargeDTO importerDepuisCalc(@NotNull File ficCalc, @NotNull RapportImportPlanCharge rapport) throws ServiceException {
        try {
            PlanCharge planCharge = planChargeDao.importerDepuisCalc(ficCalc, rapport);
            return PlanChargeDTO.from(planCharge);
        } catch (DTOException | DaoException e) {
            throw new ServiceException(
                    "Impossible d'importer le plan de charge depuis le fichier Calc '" + ficCalc.getAbsolutePath() + "'.",
                    e);
        }
    }

    @NotNull
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
     * @param planifications La planification actuelle, qu'on doit calculer pour la nouvelle date d'état.
     * @param dateEtat       La (nouvelle) date d'état de la planification.
     * @return La (nouvelle) planification pour la (nouvelle) date d'état fournie.
     */
    @NotNull
    public PlanificationsDTO replanifier(@NotNull PlanificationsDTO planifications, @NotNull LocalDate dateEtat) {

        // Si la date d'état a changé, la planification change forcément aussi : il faut ajouter ou retirer des périodes de planification,
        // et initialiser les charges de chaque tâche en cas d'ajout :
        final LocalDate debutPlanif = dateEtat;
        final LocalDate finPlanif = dateEtat.plusDays(Planifications.NBR_SEMAINES_PLANIFIEES * 7); // TODO FDA 2017/07 [issue#26:PeriodeHebdo/Trim]
        planifications.keySet().parallelStream()
                .forEach(tache -> {
                    Map<LocalDate, Double> planifTache = planifications.get(tache);

                    // On supprime les périodes qui sont en dehors du calendrier de planification :
                    Set<LocalDate> periodesASupprimer = new TreeSet<>(); // Un TreeSet pour garder le tri (par date), juste pour faciliter le débogage.
                    for (LocalDate debutPeriodeTache : planifTache.keySet()) {
                        if (debutPeriodeTache.isBefore(debutPlanif) || debutPeriodeTache.isAfter(finPlanif)) {
                            periodesASupprimer.add(debutPeriodeTache);
                        }
                    }
                    periodesASupprimer.forEach(debutPeriodeTache -> {
                        planifTache.remove(debutPeriodeTache);
//                        LOGGER.debug("Période commençant le {} supprimée pour la tâche {}.", debutPeriodeTache, tache.noTache());
                    });

                    // On ajoute les planifications qui manquent au calendrier :
                    for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
                        LocalDate debutPeriodeTache = dateEtat.plusDays((noSemaine - 1) * 7);// FIXME FDA 2017/07  Ne marche que quand les périodes sont des semaines, pas pour les trimestres.
                        if (!planifTache.containsKey(debutPeriodeTache)) {
                            Double chargeTachePeriode = nouvelleCharge(tache, debutPeriodeTache);
                            if (chargeTachePeriode != null) {
                                planifTache.put(debutPeriodeTache, chargeTachePeriode);
//                            LOGGER.debug("Période commençant le {} ajoutée pour la tâche {}, chargée à {}.", debutPeriodeTache, tache.noTache(), chargeTachePeriode);
                            }
                        }
                    }
                });

        return planifications;
    }

    @Null
    private Double nouvelleCharge(@NotNull TacheDTO tache, @NotNull LocalDate debutPeriode) {

        if (!tache.estProvision()) {
            return null;
        }

        assert tache.estProvision();
        if ((tache.getDebut() != null) && tache.getDebut().isAfter(debutPeriode)) {
            return null;
        }
        if (tache.getEcheance().isBefore(debutPeriode)) {
            return null;
        }
        return 77.75; // FIXME FDA 2017/07 Retourner la charge pour la période.
    }
}
