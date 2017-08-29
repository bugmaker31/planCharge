package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.calculateur;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;
import fr.gouv.agriculture.dal.ct.ihm.controller.calculateur.AbstractCalculateur;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ChargesController;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.Objects;
import javafx.beans.property.ObjectProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@SuppressWarnings("ClassHasNoToStringMethod")
public class CalculateurCharges extends AbstractCalculateur {

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
    public void calculer() throws IhmException {
        if (!estActif()) {
            LOGGER.debug("Calculateur désactivé, donc sans effet.");
            return;
        }

        LOGGER.debug("Calcul des charges : ");
        for (int noSemaine = 1; noSemaine <= PlanChargeIhm.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            calculer(noSemaine);
        }
        LOGGER.debug("Charges calculées.");
    }

    private void calculer(int noSemaine) throws IhmException {
        if (!estActif()) {
            LOGGER.debug("Calculateur désactivé, donc sans effet.");
            return;
        }
//        LOGGER.debug("Calcul des charges pour la semaine {} : ", noSemaine);
        for (RessourceHumaineBean ressourceHumaineBean : planChargeBean.getRessourcesHumainesBeans()) {
            calculer(ressourceHumaineBean, noSemaine);
        }

        LOGGER.debug("Charges calculées pour la semaine {}.", noSemaine);
    }

    public void calculer(@NotNull RessourceHumaineBean ressHumBean) throws IhmException {
        if (!estActif()) {
            LOGGER.debug("Calculateur désactivé, donc sans effet.");
            return;
        }
//        LOGGER.debug("Calcul des charges pour la ressource {} : ", ressHumBean);
        for (int noSemaine = 1; noSemaine <= PlanChargeIhm.NBR_SEMAINES_PLANIFIEES; noSemaine++) {
            calculer(ressHumBean, noSemaine);
        }
//        LOGGER.debug("Charges calculées pour la ressource {}.", ressHumBean);
    }

    public void calculer(@NotNull RessourceHumaineBean ressHumBean, int noSemaine) throws IhmException {
        if (!estActif()) {
            LOGGER.debug("Calculateur désactivé, donc sans effet.");
            return;
        }
//        LOGGER.debug("Calcul des charges pour la ressource {} et la semaine n° {} : ", ressHumBean, noSemaine);

        LocalDate debutPeriode = planChargeBean.dateEtat().plusDays(7 * (noSemaine - 1)); // TODO FDA 2017/08 [issue#26:PeriodeHebdo/Trim]
        LocalDate finPeriode = debutPeriode.plusDays(7); // TODO FDA 2017/08 [issue#26:PeriodeHebdo/Trim]

        LocalDate debutMission = Objects.value(ressHumBean.debutMissionProperty(), ObjectProperty::get);
        LocalDate finMission = Objects.value(ressHumBean.finMissionProperty(), ObjectProperty::get);

        // TODO FDA 2017/08 Coder.

//        LOGGER.debug("Charges calculées pour la ressource {} et la semaine n° {}.", ressHumBean, noSemaine);
    }


    public void calculerDispoProfils(@NotNull LocalDate debutPeriode, @NotNull LocalDate finPeriode) throws IhmException {
        if (!estActif()) {
            LOGGER.debug("Calculateur désactivé, donc sans effet.");
            return;
        }

//        LOGGER.debug("Calcul des charges pour les profils et la période commençant le n° {} : ", debutPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE));

        // TODO FDA 2017/08 Coder.

//        LOGGER.debug("Charges calculées pour les profils et la période commençant le n° {} : ", debutPeriode.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

}