package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation.ActionAnnulable;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement.ActionRetablissable;

/**
 * Created by frederic.danna on 17/05/2017.
 *
 * @author frederic.danna
 */
public abstract class ActionUtilisateur implements ActionAnnulable, ActionRetablissable /*, ActionRepetable TODO FDA 2017/05 Décommenter, une fois codé. */ {


    /**
     * Texte notamment affiché dans les libellés des menus "Annuler", "Rétablir" et "Répéter.
     * <p>Gagne à être surchargé par chaque sous-classe.</p>
     * @return Le texte décrivant cette action.
     */
    public String getTexte() {
        return this.getClass().getSimpleName();
    }

    // Juste pour faciliter le débogage.
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
