package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller.suiviActionsUtilisateur;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;

/**
 * Created by frederic.danna on 21/05/2017.
 *
 * @author frederic.danna
 */
public class SuiviActionsUtilisateurException extends IhmException {
    public SuiviActionsUtilisateurException(Throwable cause) {
        super(cause);
    }

    public SuiviActionsUtilisateurException(String message) {
        super(message);
    }

    @SuppressWarnings("OverloadedVarargsMethod")
    public SuiviActionsUtilisateurException(String message, Object... args) {
        super(message, args);
    }

    public SuiviActionsUtilisateurException(String message, Exception cause) {
        super(message, cause);
    }

    @SuppressWarnings("OverloadedVarargsMethod")
    public SuiviActionsUtilisateurException(String message, Throwable cause, Object... args) {
        super(message, cause, args);
    }
}
