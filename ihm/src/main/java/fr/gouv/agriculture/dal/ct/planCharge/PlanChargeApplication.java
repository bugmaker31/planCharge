package fr.gouv.agriculture.dal.ct.planCharge;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;
import org.springframework.context.ApplicationContext;

/**
 * Created by frederic.danna on 23/04/2017.
 */
public class PlanChargeApplication {

    private PlanChargeIhm ihm;
    private ApplicationContext context;

    public PlanChargeApplication() {
        super();
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
}
