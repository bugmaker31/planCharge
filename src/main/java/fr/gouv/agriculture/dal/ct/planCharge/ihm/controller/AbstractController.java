package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;

/**
 * Created by frederic.danna on 23/04/2017.
 */
public abstract class AbstractController {

    private PlanChargeIhm application;

    public PlanChargeIhm getApplication() {
        return application;
    }

    public void setApplication(PlanChargeIhm application) {
        this.application = application;
    }
}
