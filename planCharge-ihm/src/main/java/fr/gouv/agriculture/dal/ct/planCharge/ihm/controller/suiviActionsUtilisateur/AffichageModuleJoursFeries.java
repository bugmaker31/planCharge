package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.ApplicationController;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public class AffichageModuleJoursFeries extends AffichageModule {

    public AffichageModuleJoursFeries(ApplicationController.NomModule nomModulePrecedent) {
        super(nomModulePrecedent);
    }

    @NotNull
    @Override
    ApplicationController.NomModule getNomModule() {
        return ApplicationController.NomModule.joursFeries;
    }

}