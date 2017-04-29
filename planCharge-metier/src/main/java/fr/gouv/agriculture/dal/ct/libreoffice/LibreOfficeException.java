package fr.gouv.agriculture.dal.ct.libreoffice;

/**
 * Created by frederic.danna on 11/03/2017.
 *
 * @author frederic.danna
 */
public class LibreOfficeException extends Exception {
    public LibreOfficeException(String message) {
        super(message);
    }

    public LibreOfficeException(String message, Exception cause) {
        super(message, cause);
    }
}
