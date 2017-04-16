package fr.gouv.agriculture.dal.ct.planCharge.ihm;

/**
 * Created by frederic.danna on 16/04/2017.
 */
public class IhmException extends RuntimeException {
    public IhmException(String message) {
        super(message);
    }
    public IhmException(String message, Exception cause) {
        super(message, cause);
    }
}
