package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * Created by frederic.danna on 11/03/2017.
 *
 * @author frederic.danna
 */
@SuppressWarnings("ClassWithTooManyMethods")
public class TotalTacheBean extends AbstractBean<AbstractDTO, TotalTacheBean> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TotalTacheBean.class);

    private IntegerProperty nbrTachesATraiter = new SimpleIntegerProperty(0);


    public TotalTacheBean() {
    }


    public int getNbrTachesATraiter() {
        return nbrTachesATraiter.get();
    }

    public IntegerProperty nbrTachesATraiterProperty() {
        return nbrTachesATraiter;
    }

    public void setNbrTachesATraiter(int nbrTachesATraiter) {
        this.nbrTachesATraiter.set(nbrTachesATraiter);
    }


    // AbstractBean :

    @NotNull
    @Override
    public AbstractDTO toDto() throws BeanException {
        return null; // TODO FDA 2017/08 Trouver mieux comme code.
    }

    @NotNull
    @Override
    public TotalTacheBean fromDto(@NotNull AbstractDTO dto) throws BeanException {
        return null; // TODO FDA 2017/08 Trouver mieux comme code.
    }

}
