package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.retablissement;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur.SuiviActionsUtilisateurException;

/**
 * Created by frederic.danna on 21/05/2017.
 *
 * @author frederic.danna
 */
public class RetablissementActionException extends SuiviActionsUtilisateurException {

    @SuppressWarnings("unused")
    public RetablissementActionException(Throwable cause) {
        super(cause);
    }

    @SuppressWarnings("unused")
    public RetablissementActionException(String message) {
        super(message);
    }

    @SuppressWarnings({"unused", "OverloadedVarargsMethod"})
    public RetablissementActionException(String message, Object... args) {
        super(message, args);
    }

    @SuppressWarnings("unused")
    public RetablissementActionException(String message, Exception cause) {
        super(message, cause);
    }

    @SuppressWarnings({"unused", "OverloadedVarargsMethod"})
    public RetablissementActionException(String message, Throwable cause, Object... args) {
        super(message, cause, args);
    }
}
