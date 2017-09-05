package fr.gouv.agriculture.dal.ct.metier.dao;

/**
 * Created by frederic.danna on 22/04/2017.
 */
public class EntityAlreadyExistException extends DaoException {

    public EntityAlreadyExistException(String message) {
        super(message);
    }
}
