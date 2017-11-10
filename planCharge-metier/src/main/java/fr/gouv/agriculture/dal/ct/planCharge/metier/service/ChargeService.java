package fr.gouv.agriculture.dal.ct.planCharge.metier.service;

import fr.gouv.agriculture.dal.ct.metier.MetierException;
import fr.gouv.agriculture.dal.ct.metier.dao.DaoException;
import fr.gouv.agriculture.dal.ct.metier.dto.DTOException;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.ControleurRegles;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.ViolationRegleGestion;
import fr.gouv.agriculture.dal.ct.metier.regleGestion.ViolationsReglesGestionException;
import fr.gouv.agriculture.dal.ct.metier.service.AbstractService;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.constante.TypeChangement;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.charge.PlanChargeDaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.tache.TacheDao;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dao.tache.TacheDaoException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanChargeDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.PlanificationsDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.PlanCharge;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.TacheSansPlanificationException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.tache.Tache;
import fr.gouv.agriculture.dal.ct.planCharge.util.Dates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by frederic.danna on 26/03/2017.
 */
public class ChargeService extends AbstractService {

    public static final double NBR_HEURES_OUVREES_DS_UN_JOUR = 8.0;


    private static final Logger LOGGER = LoggerFactory.getLogger(ChargeService.class);


    private static ChargeService instance;

    public static ChargeService instance() {
        if (instance == null) {
            instance = new ChargeService();
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
    private ChargeService() {
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

    // [RG_Gal_ControlerRegles:Contrôler les règles]
    @SuppressWarnings("WeakerAccess")
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
            throw new ServiceException("Impossible de sauver le plan de charge.", e);
        }
    }


    @NotNull
    public RapportImportTaches majTachesDepuisCalc(@NotNull PlanChargeDTO planCharge, @NotNull File ficCalcTaches, @NotNull RapportImportTaches rapport) throws ServiceException {
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

                    tacheImporteeDTO.setTypeChangement(TypeChangement.AJOUT);

                    // Ajout des tâches qui ont été créées depuis :
                    assert planCharge.getDateEtat() != null;
                    planCharge.getPlanifications().ajouter(tacheImporteeDTO, planCharge.getDateEtat());
                    rapport.incrNbrTachesAjoutees();
                    LOGGER.debug("Tâche {} ajoutée.", tacheImportee);
                } else {

                    tacheImporteeDTO.setTypeChangement(TypeChangement.MODIFICATION);

                    // Mise à jour des tâches qui existaient déjà avant, en gardant la planification actuelle :
                    TacheDTO tacheActuelleDTO = planCharge.getPlanifications().tache(tacheImportee.getId());
                    assert tacheActuelleDTO != null;
                    Map<LocalDate, Double> calendrierTache = planCharge.getPlanifications().calendrier(tacheActuelleDTO);
                    // NB : Il faut supprimer la tâche avant de la remettre, sinon ses attributs ne sont pas mis à jour (l'échéance, notamment).
                    // (La Map doit croire qu'il s'agit de la même entité vu qu'elles ont le même hashCode, donc un simple "put" est sans effet puisque la Map croit qu'elle contient déjà l'instance.)
                    planCharge.getPlanifications().remove(tacheActuelleDTO);

                    TacheDTO tacheAJourDTO = majTache(tacheActuelleDTO, tacheImporteeDTO);

                    planCharge.getPlanifications().put(tacheAJourDTO, calendrierTache);
                    //
                    rapport.incrNbrTachesMisesAJour();
                    LOGGER.debug("Tâche {} màj.", tacheActuelleDTO);
                }
            }
            // Suppression des tâches qui n'existent plus (terminée/annulée/etc.) :
            Set<TacheDTO> tachesActuellesASupprimer = new HashSet<>(50);
            assert planCharge.getPlanifications() != null;
            for (TacheDTO tacheActuelle : planCharge.getPlanifications().taches()) {
                if (!tachesImportees.contains(tacheActuelle.toEntity())) {
                    tachesActuellesASupprimer.add(tacheActuelle);
                } else {
                    // Tâche déjà mise à jour, dans la boucle 'for' ci-dessus.
                }
            }
            tachesActuellesASupprimer.forEach(tacheActuelle -> {
                planCharge.getPlanifications().remove(tacheActuelle);
                rapport.incrNbrTachesSupprimees();
                LOGGER.debug("Tâche {} supprimée.", tacheActuelle);
            });

        } catch (DTOException | TacheDaoException | TacheSansPlanificationException e) {
            throw new ServiceException(
                    "Impossible de màj les tâches depuis le fichier Calc '" + ficCalcTaches.getAbsolutePath() + "'.",
                    e);
        }
        return rapport;
    }

    @NotNull
    private TacheDTO majTache(@NotNull TacheDTO tacheActuelle, @NotNull TacheDTO tacheImportee) {
        // Le fichier Calc de suivi des demandes (depuis lequel on vient d'importer les tâches à jour)
        // n'a pas la certaines notions (notamment catégorie et sous-catégorie).
        // Donc on garde les infos qu'on a plutôt que celles "devinées" lors de l'import.

        tacheImportee.setCategorie(tacheActuelle.getCategorie());
        tacheImportee.setSousCategorie(tacheActuelle.getSousCategorie());

        return tacheImportee;
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
    public PlanificationsDTO replanifier(@NotNull PlanificationsDTO planifications, @NotNull LocalDate dateEtat) throws ServiceException {

        // Si la date d'état a changé, la planification change forcément aussi : il faut ajouter ou retirer des périodes de planification,
        // et initialiser les charges de chaque tâche en cas d'ajout :
        //noinspection UnnecessaryLocalVariable
        LocalDate debutPlanif = dateEtat;
        LocalDate finPlanif = dateEtat.plusDays((long) Planifications.NBR_SEMAINES_PLANIFIEES * 7L); // TODO FDA 2017/07 [issue#26:PeriodeHebdo/Trim]
        for (Map.Entry<TacheDTO, Map<LocalDate, Double>> planifEntry : planifications.entrySet()) {
            TacheDTO tache = planifEntry.getKey();
            Map<LocalDate, Double> planifTache = planifEntry.getValue();

            // On supprime les périodes qui sont en dehors de la période de planification ([date d'état.. date d'état + Planifications.NBR_SEMAINES_PLANIFIEES semaines]) :
            Set<LocalDate> periodesASupprimer = new TreeSet<>(); // Un TreeSet pour garder le tri (par date), juste pour faciliter le débogage.
            for (LocalDate debutPeriodeTache : planifTache.keySet()) {
                if (debutPeriodeTache.isBefore(debutPlanif) || debutPeriodeTache.isAfter(finPlanif)) {
                    periodesASupprimer.add(debutPeriodeTache);
                }
            }
            periodesASupprimer.forEach(debutPeriodeTache -> {
                //noinspection Convert2MethodRef
                planifTache.remove(debutPeriodeTache);
                LOGGER.debug("Période commençant le {} supprimée pour la tâche {} ({}).", debutPeriodeTache, tache.noTache(), tache.getStatut());
            });

            // On ajoute les planifications qui manquent au calendrier :
            for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
                LocalDate debutPeriodeTache = dateEtat.plusDays(((long) noSemaine - 1L) * 7L); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                if (!planifTache.containsKey(debutPeriodeTache)) {
                    LocalDate finPeriodeTache = debutPeriodeTache.plusDays(7); // TODO FDA 2017/06 [issue#26:PeriodeHebdo/Trim]
                    Double chargeTachePeriode = nouvelleCharge(tache, debutPeriodeTache, finPeriodeTache, dateEtat);
                    if (chargeTachePeriode != null) {
                        planifTache.put(debutPeriodeTache, chargeTachePeriode);
                        LOGGER.debug("Période commençant le {} ajoutée pour la tâche {} ({}) avec une charge de {}.", debutPeriodeTache.format(DateTimeFormatter.ISO_LOCAL_DATE), tache.noTache(), tache.getStatut(), chargeTachePeriode);
                    }
                }
            }
        }
        return planifications;
    }

    @Null
    private Double nouvelleCharge(@NotNull TacheDTO tache, @NotNull LocalDate debutPeriode, @NotNull LocalDate finPeriode, @NotNull LocalDate dateEtat) throws ServiceException {
        if (!tache.estProvision()) {
            return null;
        }
        assert tache.estProvision();

        return provision(tache, debutPeriode, finPeriode, dateEtat);
    }

    public double provision(@NotNull TacheDTO tache, @NotNull LocalDate debutPeriode, @NotNull LocalDate finPeriode, @NotNull LocalDate dateEtat) throws ServiceException {

        if ((tache.getDebut() != null) && tache.getDebut().isAfter(finPeriode)) {
            return 0.0;
        }
        if ((tache.getEcheance() != null) && tache.getEcheance().isBefore(debutPeriode)) {
            return 0.0;
        }

        double chargeTache = tache.getCharge();
        if (chargeTache == 0.0) {
            return 0.0;
        }

        LocalDate debutTache = tache.getDebut();
        if (debutTache == null) {
//            throw new ServiceException("Impossible de calculer la provision de la tâche " + tache.noTache() + ", car elle n'a pas de date de début.");
            debutTache = dateEtat; // TODO FDA 2017/08 Confirmer.
        }

        LocalDate echeanceTache = tache.getEcheance();

        LocalDate debutProvision = Dates.max(debutTache, dateEtat);
        LocalDate finProvision = echeanceTache.plusDays(1L); // "+1" car le jour d'échéance est inclus.

//        Cf. http://docs.oracle.com/javase/tutorial/datetime/iso/period.html
        long dureeRestanteTacheEnJours = ChronoUnit.DAYS.between(debutProvision, finProvision);
        double provisionParJour = chargeTache / (double) dureeRestanteTacheEnJours;

        long dureePeriodeEnJours = ChronoUnit.DAYS.between(Dates.max(debutPeriode, dateEtat), Dates.min(finPeriode, finProvision));

        return provisionParJour * (double) dureePeriodeEnJours;
    }

    public Double chargeArrondie(@NotNull Double charge) {
        return (double) Math.round(charge * NBR_HEURES_OUVREES_DS_UN_JOUR) / NBR_HEURES_OUVREES_DS_UN_JOUR;
    }
}
