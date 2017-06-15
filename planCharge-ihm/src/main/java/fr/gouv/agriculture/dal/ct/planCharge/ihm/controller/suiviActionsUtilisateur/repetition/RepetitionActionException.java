package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.repetition;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.SuiviActionsUtilisateurException;

/**
 * Created by frederic.danna on 21/05/2017.
 *
 * @author frederic.danna
 */
public class RepetitionActionException extends SuiviActionsUtilisateurException {

    @SuppressWarnings("unused")
    public RepetitionActionException(Throwable cause) {
        super(cause);
    }

    @SuppressWarnings("unused")
    public RepetitionActionException(String message) {
        super(message);
    }

    @SuppressWarnings("unused")
    public RepetitionActionException(String message, Exception cause) {
        super(message, cause);
    }

}
