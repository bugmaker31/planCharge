package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.PlanChargeApplication;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;

/**
 * Created by frederic.danna on 23/04/2017.
 */
public abstract class AbstractController {

    private PlanChargeApplication application;

    public PlanChargeApplication getApplication() {
        return application;
    }

    public void setApplication(PlanChargeApplication application) {
        this.application = application;
    }

    public PlanChargeIhm getApplicationIhm() {
        return application.getIhm();
    }
}
