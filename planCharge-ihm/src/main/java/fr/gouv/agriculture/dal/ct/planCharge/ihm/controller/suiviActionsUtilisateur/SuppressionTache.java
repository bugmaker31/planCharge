package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.NotImplementedException;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.RetablissementActionException;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public class SuppressionTache extends ModificationPlanification {

    @Override
    public void annuler() {
        throw new NotImplementedException();
    }

    @Override
    public void retablir() throws RetablissementActionException {
        throw new NotImplementedException();
    }
}
