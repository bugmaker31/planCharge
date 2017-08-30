package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.calculateur;

import fr.gouv.agriculture.dal.ct.ihm.controller.ControllerException;
import fr.gouv.agriculture.dal.ct.ihm.controller.calculateur.Calculateur;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ChargesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanificationTacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache.TacheBean;
import fr.gouv.agriculture.dal.ct.planCharge.metier.service.ChargeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

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
    private final ChargesController getChargesController() {
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

    private void calculerProvision(@NotNull PlanificationTacheBean planificationTacheBean) {
        if (!planificationTacheBean.getTacheBean().estProvision()) {
            LOGGER.debug("La tâche {} n'est pas une provision, donc rien à calculer.", planificationTacheBean.noTache());
            return;
        }
        LOGGER.debug("Calcul des charges à provisionner pour la tâche {} : ", planificationTacheBean.noTache());

        TacheBean tacheBean = planificationTacheBean.getTacheBean();
/* TODO FDA 2017/08 Coder.
        LocalDate debutPeriode;
        LocalDate finPeriode;
        LocalDate dateEtat;
        chargeService.provision(tacheBean, debutPeriode, finPeriode, dateEtat);
*/
        LOGGER.debug("Charges à provisionner calculées pour la tâche {} : ", planificationTacheBean.noTache());
    }

    private void calculerSurcharges() {
        // TODO FDA 2017/08 Coder.
/*
        calculerSurchargesRessources();
        calculerSurchargesProfils();
*/
    }
}