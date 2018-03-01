package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.suiviActionsUtilisateurSurPlanCharge;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.charge.PlanChargeBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.NotImplementedException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.ActionAnnulable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.ActionRetablissable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.RetablissementActionException;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public class ImportTaches<T> extends ModificationEnMassePlanCharge<PlanChargeBean, T> implements ActionAnnulable, ActionRetablissable {

    @Override
    public void annuler() {
        throw new NotImplementedException();
    }


    @Override
    public void retablir() throws RetablissementActionException {
        throw new NotImplementedException();
    }
}
