package fr.gouv.agriculture.dal.ct.metier.modele;

import fr.gouv.agriculture.dal.ct.metier.MetierException;

/**
 * Created by frederic.danna on 16/04/2017.
 */
public class ModeleException extends MetierException {

    public ModeleException(String message) {
        super(message);
    }
    public ModeleException(String message, Exception cause) {
        super(message, cause);
    }
}
