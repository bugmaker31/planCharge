package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractDisponibilitesRessourceBean<D extends AbstractDTO, B extends AbstractDisponibilitesRessourceBean<D, B, T>, T> extends AbstractDisponibilitesBean<D, B, T> {


    // Fields :

    @NotNull
    private Property<RessourceHumaineBean> ressourceHumaineBean = new SimpleObjectProperty<>();


    // Constructors:

    public AbstractDisponibilitesRessourceBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull Map<LocalDate, T> calendrier) {
        super(calendrier);
        this.ressourceHumaineBean.setValue(ressourceHumaineBean);
    }

    public AbstractDisponibilitesRessourceBean(@NotNull RessourceHumaineBean ressourceHumaineBean) {
        this(ressourceHumaineBean, new TreeMap<>()); // TreeMap juste pour trier afin de faciliter le débogage.
    }


    // Methods:

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;
//        if (!super.equals(o)) return false;

        //noinspection unchecked
        AbstractDisponibilitesRessourceBean<D, B, T> that = (AbstractDisponibilitesRessourceBean<D, B, T>) o;

        return getRessourceHumaineBean().equals(that.getRessourceHumaineBean());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + getRessourceHumaineBean().hashCode();
        return result;
    }


    // Getters/Setters:

    @NotNull
    public RessourceHumaineBean getRessourceHumaineBean() {
        return ressourceHumaineBean.getValue();
    }

    @NotNull
    public Property<RessourceHumaineBean> ressourceHumaineBeanProperty() {
        return ressourceHumaineBean;
    }


    // Utilitie's methods:

    @Override
    public String toString() {
        return ressourceHumaineBean + " : " + super.toString();
    }
}
