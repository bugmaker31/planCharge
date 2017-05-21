package fr.gouv.agriculture.dal.ct.planCharge.util.cloning;

/**
 * Created by frederic.danna on 21/05/2017.
 *
 * @author frederic.danna
 */
public class CopieException extends Exception {

    public CopieException(String message) {
        super(message);
    }

    public CopieException(String message, Throwable cause) {
        super(message, cause);
    }
}
