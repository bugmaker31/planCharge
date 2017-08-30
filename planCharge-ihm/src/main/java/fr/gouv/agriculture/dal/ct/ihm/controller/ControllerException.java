package fr.gouv.agriculture.dal.ct.ihm.controller;

import fr.gouv.agriculture.dal.ct.ihm.IhmException;

/**
 * Created by frederic.danna on 16/04/2017.
 */
public class ControllerException extends IhmException {

    public ControllerException(String message) {
        super(message);
    }

    public ControllerException(String message, Exception cause) {
        super(message, cause);
    }
}
