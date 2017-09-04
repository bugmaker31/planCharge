package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.ihm.model.BeanException;
import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceBean;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractCalendrierParRessourceBean<R extends RessourceBean, D extends AbstractDTO, B extends AbstractCalendrierParRessourceBean<R, D, B, P>, P
        extends Property> extends AbstractCalendrierBean<D, B, P> {

    // Fields :

    @NotNull
    private Property<R> ressourceBean = new SimpleObjectProperty<>();


    // Constructors:

    public AbstractCalendrierParRessourceBean(@NotNull R ressourceBean, @NotNull Map<LocalDate, P> calendrier) throws BeanException {
        super(calendrier);
        this.ressourceBean.setValue(ressourceBean);
    }

    public AbstractCalendrierParRessourceBean(@NotNull R ressourceBean) throws BeanException {
        this(ressourceBean, new TreeMap<>()); // TreeMap juste pour trier afin de faciliter le débogage.
    }


    // Methods:

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;
//        if (!super.equals(o)) return false;

        //noinspection unchecked
        AbstractCalendrierParRessourceBean<R, D, B, P> that = (AbstractCalendrierParRessourceBean<R, D, B, P>) o;

        return getRessourceBean().equals(that.getRessourceBean());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + ((ressourceBean.getValue() == null) ? 0 : ressourceBean.getValue().hashCode());
        return result;
    }


    // Getters/Setters:


    @NotNull
    public R getRessourceBean() {
        return ressourceBean.getValue();
    }

    @NotNull
    public Property<R> ressourceBeanProperty() {
        return ressourceBean;
    }


    // Methods:


    // Utilitie's methods:

    @Override
    public String toString() {
        return ressourceBean.getValue() + " : " + super.toString();
    }
}
