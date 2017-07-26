package fr.gouv.agriculture.dal.ct.planCharge.ihm;

/**
 * Created by frederic.danna on 16/04/2017.
 * @author frederic.danna
 */
public class IhmException extends Exception {
    public IhmException(Throwable cause) {
        super(cause);
    }

    public IhmException(String message) {
        super(message);
    }

    public IhmException(String message, Throwable cause) {
        super(message, cause);
    }

}
