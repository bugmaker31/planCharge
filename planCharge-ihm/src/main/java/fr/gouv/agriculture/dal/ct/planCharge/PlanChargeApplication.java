package fr.gouv.agriculture.dal.ct.planCharge;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.PlanificationBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Created by frederic.danna on 23/04/2017.
 */
public class PlanChargeApplication {

    @Autowired
    private PlanChargeBean planChargeBean;
    private PlanChargeIhm ihm;
    private ApplicationContext context;

    public PlanChargeBean getPlanChargeBean() {
        return planChargeBean;
    }

    public void setPlanChargeBean(PlanChargeBean planChargeBean) {
        this.planChargeBean = planChargeBean;
    }

    public PlanChargeIhm getIhm() {
        return ihm;
    }

    public void setIhm(PlanChargeIhm ihm) {
        this.ihm = ihm;
    }

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public PlanChargeApplication() {
        super();
    }
}
