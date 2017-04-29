package libreoffice;

/**
 * Created by frederic.danna on 29/04/2017.
 */
/*
On étend une RuntimeException pour éviter d'avoir à retoucher toutes les méthodes
récupérées de Lo.java (@ Andrew Davison, ad@fivedots.coe.psu.ac.th, February 2015)
 */
public class LOException extends RuntimeException {

    public LOException(String message) {
        super(message);
    }

    public LOException(String message, Throwable cause) {
        super(message, cause);
    }
}
