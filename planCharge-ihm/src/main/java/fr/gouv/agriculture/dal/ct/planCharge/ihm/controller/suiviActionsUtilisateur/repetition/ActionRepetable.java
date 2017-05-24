package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.repetition;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.ActionUtilisateur;

/**
 * Created by frederic.danna on 23/05/2017.
 *
 * @author frederic.danna
 */
public interface ActionRepetable extends ActionUtilisateur {

    /**
     * Répéter (<i>do again</i>) {@link ActionUtilisateur l'action de l'utilisateur.}
     * @throws RepetitionActionException Si impossible de répéter l'action.
     */
    void repeter() throws RepetitionActionException;

}
