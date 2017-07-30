package fr.gouv.agriculture.dal.ct.metier.dao;

/**
 * Created by frederic.danna on 22/04/2017.
 */
public class EntityNotFoundException extends DaoException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Exception cause) {
        super(message, cause);
    }
}
