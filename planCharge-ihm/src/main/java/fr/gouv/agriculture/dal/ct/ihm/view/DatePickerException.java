package fr.gouv.agriculture.dal.ct.ihm.view;

/**
 * Created by frederic.danna on 02/07/2017.
 */
// TODO FDA 2017/07 Trouver mieux qu'étendre RuntimeException.
public class DatePickerException extends RuntimeException {
    public DatePickerException(String message) {
        super(message);
    }
}
