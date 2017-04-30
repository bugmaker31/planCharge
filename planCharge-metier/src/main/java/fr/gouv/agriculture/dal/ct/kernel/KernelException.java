package fr.gouv.agriculture.dal.ct.kernel;

/**
 * Created by frederic.danna on 30/04/2017.
 */
public class KernelException extends Exception {

    public KernelException(String message) {
        super(message);
    }

    public KernelException(String message, Throwable cause) {
        super(message, cause);
    }
}
