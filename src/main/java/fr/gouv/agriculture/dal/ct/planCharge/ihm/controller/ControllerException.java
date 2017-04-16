package fr.gouv.agriculture.dal.ct.planCharge.ihm.controller;

import fr.gouv.agriculture.dal.ct.planCharge.ihm.IhmException;

/**
 * Created by frederic.danna on 16/04/2017.
 */
public class ControllerException extends IhmException {
    public ControllerException(String message, Exception cause) {
        super(message, cause);
    }
}
