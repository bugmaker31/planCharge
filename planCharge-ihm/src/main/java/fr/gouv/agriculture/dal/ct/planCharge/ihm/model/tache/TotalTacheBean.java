package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.tache;

import fr.gouv.agriculture.dal.ct.ihm.model.AbstractBean;
import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
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

    private LongProperty nbrTachesATraiter = new SimpleLongProperty(0);
    private DoubleProperty totalResteAFaire = new SimpleDoubleProperty(0);


    public TotalTacheBean() {
        super();
    }


    public long getNbrTachesATraiter() {
        return nbrTachesATraiter.get();
    }

    public LongProperty nbrTachesATraiterProperty() {
        return nbrTachesATraiter;
    }

    public void setNbrTachesATraiter(long nbrTachesATraiter) {
        this.nbrTachesATraiter.set(nbrTachesATraiter);
    }

    public double getTotalResteAFaire() {
        return totalResteAFaire.get();
    }

    public DoubleProperty totalResteAFaireProperty() {
        return totalResteAFaire;
    }

    public void setTotalResteAFaire(double totalResteAFaire) {
        this.totalResteAFaire.set(totalResteAFaire);
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
