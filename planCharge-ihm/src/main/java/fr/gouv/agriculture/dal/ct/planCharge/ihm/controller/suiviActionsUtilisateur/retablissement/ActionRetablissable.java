package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.ActionUtilisateur;

/**
 * Created by frederic.danna on 23/05/2017.
 *
 * @author frederic.danna
 */
public interface ActionRetablissable extends  ActionUtilisateur {

    /**
     * Rétablir (<i>redo</i>) {@link ActionUtilisateur l'action de l'utilisateur.}
     * @throws RetablissementActionException Si impossible de rétablir l'action.
     */
    void retablir() throws RetablissementActionException;

}
