package fr.gouv.agriculture.dal.ct.planCharge.metier.dao;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by frederic.danna on 03/04/2017.
 * @author frederic.danna
 */
public abstract class MockedDao<E extends AbstractEntity<EI>, EI extends Serializable> extends AbstractDao<E, EI> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MockedDao.class);

    protected abstract Map<EI, E> getCache();

    protected abstract E newEntity(EI id) throws DaoException;

    @NotNull
    public E load(@NotNull EI id) throws EntityNotFoundException, DaoException {

        if (getCache().containsKey(id)) {
            LOGGER.debug("Entité '" + this.getClass().getCanonicalName() + "' retrouvée dans le cache : '" + id + "'.");
            return (E) getCache().get(id);
        }

        E nouvelleEntite = newEntity(id);

        getCache().put(id, nouvelleEntite);
        LOGGER.debug("Entité '" + this.getClass().getCanonicalName() + "' ajoutée au cache : '" + id + "'.");

        return nouvelleEntite;
    }

    @NotNull
    public List<E> list() throws DaoException {
        return new ArrayList<>(getCache().values());
    }
}
