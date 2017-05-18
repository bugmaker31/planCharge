package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public class ChargementPlanCharge extends ModificationEnMassePlanCharge {

    private PlanChargeBean planChargeBean;


    public ChargementPlanCharge(PlanChargeBean planChargeBean) {
        super();
        this.planChargeBean = planChargeBean; // TODO FDA 2017/05 Vraiment besoin de mémoriser tout le plan de charge ?
    }


    public PlanChargeBean getPlanChargeBean() {
        return planChargeBean;
    }

    @Override
    public String getTexte() {
        return "le chargement du plan de charge";
    }
}
