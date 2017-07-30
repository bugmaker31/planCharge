package fr.gouv.agriculture.dal.ct.ihm.model;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;

public class BeanException extends IhmException {

    public BeanException(Throwable cause) {
        super(cause);
    }

    public BeanException(String message) {
        super(message);
    }

    public BeanException(String message, Throwable cause) {
        super(message, cause);
    }
}
