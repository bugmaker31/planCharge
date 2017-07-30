package fr.gouv.agriculture.dal.ct.metier;

/**
 * Created by frederic.danna on 16/04/2017.
 */
public class MetierException extends Exception {

    public MetierException(String message) {
        super(message);
    }

    public MetierException(String message, Exception cause) {
        super(message, cause);
    }
}
