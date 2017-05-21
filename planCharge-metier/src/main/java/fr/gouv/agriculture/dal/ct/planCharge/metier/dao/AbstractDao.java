package fr.gouv.agriculture.dal.ct.planCharge.metier.dao;

import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.AbstractEntity;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.ModeleException;
import fr.gouv.agriculture.dal.ct.planCharge.metier.modele.referentiels.Importance;
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
public abstract class AbstractDao<E extends AbstractEntity<EI>, EI extends Serializable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDao.class);

/* TODO FDA 2017/05 Ajouter cette méthode (et d'autres) une fois débouchonné.
    @NotNull
    abstract public E load(@NotNull EI id) throws EntityNotFoundException, DaoException;

    @NotNull
    abstract public List<E> list() throws DaoException;
*/
}
