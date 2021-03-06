package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.suiviActionsUtilisateurSurPlanCharge;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.SuiviActionsUtilisateurException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.ActionAnnulable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.AnnulationActionException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.cloning.CopieException;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public class ChargementPlanCharge extends ModificationEnMassePlanCharge implements ActionAnnulable {

    private PlanChargeBean planChargeBeanPrecedent;

    //@Autowired
    @SuppressWarnings("FieldCanBeLocal")
    @NotNull
    private final PlanChargeBean planChargeBean = PlanChargeBean.instance();


    public ChargementPlanCharge(PlanChargeBean planChargeBeanPrecedent) throws SuiviActionsUtilisateurException {
        super();
        // TODO FDA 2017/05 Vraiment besoin de mémoriser tout le plan de charge ?
        try {
            this.planChargeBeanPrecedent = PlanChargeBean.copier(planChargeBeanPrecedent);
        } catch (CopieException e) {
            throw new AnnulationActionException("Impossible de mémoriser le plan de charge (pour annuler l'action " + getTexte() + ", éventuellement).", e);
        }
    }


    @Override
    public String getTexte() {
        return "le chargement du plan de charge";
    }


    @Override
    public void annuler() throws AnnulationActionException {
        try {
            PlanChargeBean.copier(planChargeBeanPrecedent, planChargeBean);
        } catch (CopieException e) {
            throw new AnnulationActionException("Impossible d'annuler l'action " + getTexte() + ".", e);
        }
    }

}
