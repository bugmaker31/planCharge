package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.ActionUtilisateur;

/**
 * Created by frederic.danna on 21/05/2017.
 *
 * @author frederic.danna
 */
public interface ActionAnnulable extends ActionUtilisateur {

    /**
     * Annuler {@link ActionUtilisateur l'action de l'utilisateur.}
     * @throws AnnulationActionException Si impossible d'annuler l'action.
     */
    void annuler() throws AnnulationActionException;

}
