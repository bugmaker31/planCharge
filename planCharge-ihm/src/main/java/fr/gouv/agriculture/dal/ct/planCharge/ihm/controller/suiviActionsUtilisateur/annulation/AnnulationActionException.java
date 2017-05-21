package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.annulation;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.SuiviActionsUtilisateurException;

/**
 * Created by frederic.danna on 21/05/2017.
 *
 * @author frederic.danna
 */
public class AnnulationActionException extends SuiviActionsUtilisateurException {

    @SuppressWarnings("unused")
    public AnnulationActionException(Throwable cause) {
        super(cause);
    }

    @SuppressWarnings("unused")
    public AnnulationActionException(String message) {
        super(message);
    }

    @SuppressWarnings({"unused", "OverloadedVarargsMethod"})
    public AnnulationActionException(String message, Object... args) {
        super(message, args);
    }

    @SuppressWarnings("unused")
    public AnnulationActionException(String message, Exception cause) {
        super(message, cause);
    }

    @SuppressWarnings({"unused", "OverloadedVarargsMethod"})
    public AnnulationActionException(String message, Throwable cause, Object... args) {
        super(message, cause, args);
    }
}
