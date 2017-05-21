package fr.gouv.agriculture.dal.ct.planCharge.ihm;

/**
 * Created by frederic.danna on 16/04/2017.
 */
public class IhmException extends Exception {
    public IhmException(Throwable cause) {
        super(cause);
    }

    public IhmException(String message) {
        super(message);
    }

    public IhmException(String message, Object... args) {
        super(String.format(message, args));
    }

    public IhmException(String message, Exception cause) {
        super(message, cause);
    }

    public IhmException(String message, Throwable cause, Object... args) {
        super(String.format(message, args), cause);
    }
}
