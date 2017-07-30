package fr.gouv.agriculture.dal.ct.metier.dao;

import fr.gouv.agriculture.dal.ct.metier.modele.AbstractEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static javax.swing.UIManager.get;

/**
 * Created by frederic.danna on 03/04/2017.
 *
 * @author frederic.danna
 */
public abstract class AbstractDao<E extends AbstractEntity<EI, E>, EI extends Serializable> implements DataAcessObject {

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDao.class);


    @NotNull
    protected abstract Map<EI, E> getCache();

    @Null
    public E get(@NotNull EI id) throws EntityNotFoundException {

        if (getCache().containsKey(id)) {
            LOGGER.debug("Entité '" + this.getClass().getCanonicalName() + "' retrouvée dans le cache : '" + id + "'.");
            return getCache().get(id);
        }
        return null;
    }


    @NotNull
    public E load(@NotNull EI id) throws EntityNotFoundException {
        E entity = get(id);
        if (entity == null) {
            throw new EntityNotFoundException("Entité '" + this.getClass().getCanonicalName() + "' n'existe pas avec l'ID '" + id + "'.");
        }
        return entity;
    }

    @NotNull
    public List<E> list() throws DaoException {
        // TODO FDA 2017/07 Pour l'instant, lister les valeurs revient à lister celles qu'on connaît, donc celles du cache.
        return new ArrayList<>(getCache().values());
    }

    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean exists(@NotNull EI id) throws DaoException {
        return get(id) != null;
    }

    public void createOrUpdate(@NotNull E entity) throws DaoException {
        getCache().put(entity.getIdentity(), entity);
    }
}
