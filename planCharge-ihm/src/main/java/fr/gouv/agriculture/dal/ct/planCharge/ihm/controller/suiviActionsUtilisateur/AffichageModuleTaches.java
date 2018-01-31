package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.ihm.module.Module;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.PlanChargeIhm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public class AffichageModuleTaches extends AffichageModule {

    public AffichageModuleTaches(@Null Module modulePrecedent) {
        super(modulePrecedent);
    }

    @NotNull
    @Override
    Module getModule() {
        return PlanChargeIhm.instance().getTachesController();
    }
}
