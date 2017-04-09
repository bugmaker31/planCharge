package fr.gouv.agriculture.dal.ct.planCharge.metier.dao;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by frederic.danna on 03/04/2017.
 */
public abstract class AbstractDao<E extends AbstractEntity<EI>, EI extends Serializable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDao.class);

    protected abstract Map<EI, E> getCache();

    protected abstract E newEntity(EI id);

    final public E load(EI id) {

        if (getCache().containsKey(id)) {
            LOGGER.debug("Entité '" + this.getClass().getCanonicalName() + "' retrouvée dans le cache : '" + id + "'.");
            return (E) getCache().get(id);
        }

        // TODO FDA 2017/03 Débouchonner : retrouver depuis la couche de persistence.
        E nouvelleEntite = newEntity(id);

        getCache().put(id, nouvelleEntite);
        LOGGER.debug("Entité '" + this.getClass().getCanonicalName() + "' ajoutée au cache : '" + id + "'.");

        return nouvelleEntite;
    }

}
