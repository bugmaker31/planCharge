package fr.gouv.agriculture.dal.ct.planCharge.metier.dao;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Ressource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by frederic.danna on 03/04/2017.
 *
 * @author frederic.danna
 */
public abstract class AbstractDao<E extends AbstractEntity<EI>, EI extends Serializable> implements DataAcessObject {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDao.class);


    protected abstract Map<EI, E> getCache();


    @NotNull
    public E load(@NotNull EI id) throws EntityNotFoundException {

        if (getCache().containsKey(id)) {
            LOGGER.debug("Entité '" + this.getClass().getCanonicalName() + "' retrouvée dans le cache : '" + id + "'.");
            return getCache().get(id);
        }

        throw new EntityNotFoundException("Entité '" + this.getClass().getCanonicalName() + "' n'existe pas avec l'ID '" + id + "'.");
    }

    @NotNull
    public List<E> list() throws DaoException {
        // TODO FDA 2017/07 Pour l'instant, lister les valeurs revient à lister celles qu'on connaît, donc celles du cache.
        return new ArrayList<>(getCache().values());
    }


    public void saveOrUpdate(@NotNull E entity) throws DaoException {
        getCache().put(entity.getIdentity(), entity);
    }
}
