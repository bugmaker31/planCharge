package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.calculateur;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.controller.calculateur.Calculateur;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ChargesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.CalendrierFractionsJoursChargeParRessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ChargeService;
import fr.gouv.agriculture.dal.ct.planCharge.util.Collections;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleFloatProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("ClassHasNoToStringMethod")
public class CalculateurCharges extends Calculateur {

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculateurCharges.class);


    /*
     La couche métier :
    */

    /*
        //    @Autowired
        @NotNull
        private final DisponibilitesService disponibilitesService = DisponibilitesService.instance();
    */
//    @Autowired
    @NotNull
    private final ChargeService chargeService = ChargeService.instance();

    //    @Autowired
    @NotNull
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();

    /*
     La couche controller :
    */

    /*
        //    @Autowired
        @NotNull
        private final DisponibilitesController disponibilitesController = DisponibilitesController.instance();
    */
    //    @Autowired
    @NotNull
    private ChargesController getChargesController() {
        return ChargesController.instance();
    }


    // Constructeurs :

    public CalculateurCharges() {
        super();
        // Rien... pour l'instant.
    }


    // Méthodes :

    //    @Override
    public void calculer() throws ControllerException {
        if (!estActive()) {
            LOGGER.debug("Calculateur désactivé, donc sans effet.");
            return;
        }

        LOGGER.debug("Calcul des charges : ");

        calculerProvisions();
        calculerNbrsJoursChargeParRessource();
        calculerSurcharges();

        LOGGER.debug("Charges calculées.");
    }

    private void calculerProvisions() throws ControllerException {
        LOGGER.debug("Calcul des charges à provisionner : ");
        for (PlanificationTacheBean planificationTacheBean : planChargeBean.getPlanificationsBeans()) {
            calculerProvision(planificationTacheBean);
        }
        LOGGER.debug("Charges à provisionner calculées.");
    }

    private void calculerProvision(@NotNull PlanificationTacheBean planificationTacheBean) throws ControllerException {
        if (!planificationTacheBean.getTacheBean().estProvision()) {
            LOGGER.debug("La tâche {} n'est pas une provision, donc rien à calculer.", planificationTacheBean.noTache());
            return;
        }
        LOGGER.debug("Calcul des charges à provisionner pour la tâche {} : ", planificationTacheBean.noTache());

        for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            calculerProvision(planificationTacheBean, noSemaine);
        }
        LOGGER.debug("Charges à provisionner calculées pour la tâche {} : ", planificationTacheBean.noTache());
    }

    private void calculerProvision(@NotNull PlanificationTacheBean planificationTacheBean, int noSemaine) throws ControllerException {
        TacheDTO tacheDTO = planificationTacheBean.toDto();

        LocalDate dateEtat = planChargeBean.getDateEtat();
        if (dateEtat == null) {
            LOGGER.debug("Pas de date d'état, on ne peut pas calculer les provisions.");
            return;
        }

        LocalDate debutPeriode = dateEtat.plusDays(((long) noSemaine - 1L) * 7L); // TODO FDA 2017/09 [issue#26:PeriodeHebdo/Trim]

        LocalDate finPeriode = debutPeriode.plusDays(7L); // TODO FDA 2017/09 [issue#26:PeriodeHebdo/Trim]

        double provision;
        try {
            provision = chargeService.provision(tacheDTO, debutPeriode, finPeriode, dateEtat);
        } catch (ServiceException e) {
            throw new ControllerException("Impossible de calculer la provision pour la tâche " + planificationTacheBean.getId() + " et la semaine n° " + noSemaine + ".", e);
        }

        if (!planificationTacheBean.getCalendrier().containsKey(debutPeriode)) {
            planificationTacheBean.getCalendrier().put(debutPeriode, new SimpleDoubleProperty());
        }
        planificationTacheBean.getCalendrier().get(debutPeriode).setValue(provision);
    }


    private void calculerNbrsJoursChargeParRessource() throws ControllerException {
        for (int noSemaine = 1; noSemaine <= Planifications.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            calculerNbrsJoursChargeParRessource(noSemaine);
        }
    }

    private void calculerNbrsJoursChargeParRessource(int noSemaine) throws ControllerException {
        for (RessourceBean<?, ?> ressourceBean : planChargeBean.getRessourcesBeans()) {
            calculerNbrsJoursChargeParRessource(ressourceBean, noSemaine);
        }
    }

    private void calculerNbrsJoursChargeParRessource(@NotNull RessourceBean<?, ?> ressourceBean, int noSemaine) throws ControllerException {

        List<PlanificationTacheBean> planificationTacheRessourceBeans = planChargeBean.getPlanificationsBeans().parallelStream()
                .filter(planificationTacheBean -> (planificationTacheBean.getRessource() != null) && planificationTacheBean.getRessource().equals(ressourceBean))
                .collect(Collectors.toList());

        LocalDate dateEtat;
        try {
            dateEtat = planChargeBean.dateEtat();
        } catch (BeanException e) {
            throw new ControllerException("Impossible de calculer les nombres de jours de charge pour la ressource '" + ressourceBean.getCode() + "'.", e);
        }
        LocalDate debutPeriode = dateEtat.plusDays(((long) noSemaine - 1L) * 7L);// TODO FDA 2017/09 [issue#26:PeriodeHebdo/Trim]

        Double chargePlanifieePourLaRessource = 0.0;
        for (PlanificationTacheBean planificationTacheRessourceBean : planificationTacheRessourceBeans) {
            DoubleProperty chargePlanifieeProperty = planificationTacheRessourceBean.getCalendrier().get(debutPeriode);
            if (chargePlanifieeProperty == null) {
                continue;
            }
            Double chargePlanifiee = chargePlanifieeProperty.getValue();
            if (chargePlanifiee == 0.0) {
                continue;
            }
            chargePlanifieePourLaRessource += chargePlanifiee;
        }

        CalendrierFractionsJoursChargeParRessourceBean nbrJoursChargePourLaRessourceBean = Collections.any(
                getChargesController().getNbrsJoursChargeRsrcBeans(),
                nbrsJoursChargeParRessourceBean -> nbrsJoursChargeParRessourceBean.getRessourceBean().equals(ressourceBean),
                new ControllerException("Impossible de retrouver la ressource '" + ressourceBean.getCode() + "' dans la table des nombres de jours de charge par ressource.")
        );
        if (!nbrJoursChargePourLaRessourceBean.containsKey(debutPeriode)) {
            nbrJoursChargePourLaRessourceBean.put(debutPeriode, new SimpleFloatProperty());
        }
        nbrJoursChargePourLaRessourceBean.get(debutPeriode).setValue(chargePlanifieePourLaRessource);
    }


    private void calculerSurcharges() {
        // TODO FDA 2017/08 Coder.
/*
        calculerSurchargesRessources();
        calculerSurchargesProfils();
*/
    }
}