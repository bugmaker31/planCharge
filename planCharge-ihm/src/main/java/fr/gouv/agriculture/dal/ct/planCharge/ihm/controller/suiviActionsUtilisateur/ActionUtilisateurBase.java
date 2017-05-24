package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.ActionAnnulable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.repetition.ActionRepetable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.ActionRetablissable;

/**
 * <p>Created by frederic.danna on 17/05/2017.</p>
 *
 * @author frederic.danna
 */
public abstract class ActionUtilisateurBase implements ActionUtilisateur {


    public boolean estAnnulable() {
        return this instanceof ActionAnnulable;
    }

    public boolean estRetablissable() {
        return this instanceof ActionRetablissable;
    }

    public boolean estRepetable() {
        return this instanceof ActionRepetable;
    }

    public String getTexte() {
        return this.getClass().getSimpleName();
    }

    // Juste pour faciliter le débogage.
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
