package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.ihm.module.Module;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 20/01/2018.
 *
 * @author frederic.danna
 */
public class AffichageModuleRevisions extends AffichageModule {

    public AffichageModuleRevisions(Module modulePrecedent) {
        super(modulePrecedent);
    }

    @NotNull
    @Override
    Module getModule() {
        return PlanChargeIhm.instance().getRevisionsController();
    }
}
