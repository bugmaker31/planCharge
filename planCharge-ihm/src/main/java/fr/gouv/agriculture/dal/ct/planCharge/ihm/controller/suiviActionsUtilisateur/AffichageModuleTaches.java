package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ApplicationController;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public class AffichageModuleTaches extends AffichageModule {

    public AffichageModuleTaches(ApplicationController.NomModule nomModulePrecedent) {
        super(nomModulePrecedent);
    }

    @NotNull
    @Override
    ApplicationController.NomModule getNomModule() {
        return ApplicationController.NomModule.TACHES;
    }
}
