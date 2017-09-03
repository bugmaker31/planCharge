package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.calculateur;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.controller.calculateur.Calculateur;
import fr.gouv.agriculture.dal.ct.metier.service.ServiceException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ChargesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.dto.TacheDTO;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.charge.Planifications;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ChargeService;
import javafx.beans.property.SimpleDoubleProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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

    private void calculerNbrsJoursChargeParRessource() {

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

        LocalDate debutPeriode = dateEtat.plusDays((noSemaine - 1) * 7); // TODO FDA 2017/09 [issue#26:PeriodeHebdo/Trim]

        LocalDate finPeriode = debutPeriode.plusDays(7); // TODO FDA 2017/09 [issue#26:PeriodeHebdo/Trim]

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

    private void calculerSurcharges() {
        // TODO FDA 2017/08 Coder.
/*
        calculerSurchargesRessources();
        calculerSurchargesProfils();
*/
    }
}