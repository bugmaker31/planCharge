package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractCalendrierProfilBean<D extends AbstractDTO, B extends AbstractCalendrierProfilBean<D, B, T>, T extends Property> extends AbstractCalendrierBean<D, B, T> {


    // Fields :

    @NotNull
    private Property<ProfilBean> profilBean = new SimpleObjectProperty<>();


    // Constructors:

    public AbstractCalendrierProfilBean(@NotNull ProfilBean profilBean, @NotNull Map<LocalDate, T> calendrier) {
        super(calendrier);
        this.profilBean.setValue(profilBean);
    }

    public AbstractCalendrierProfilBean(@NotNull ProfilBean profilBean) {
        this(profilBean, new TreeMap<>()); // TreeMap juste pour trier afin de faciliter le débogage.
    }


    // Methods:

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;
//        if (!super.equals(o)) return false;

        //noinspection unchecked
        AbstractCalendrierProfilBean<D, B, T> that = (AbstractCalendrierProfilBean<D, B, T>) o;

        return getProfilBean().equals(that.getProfilBean());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + getProfilBean().hashCode();
        return result;
    }


    // Getters/Setters:

    @NotNull
    public ProfilBean getProfilBean() {
        return profilBean.getValue();
    }

    @NotNull
    public Property<ProfilBean> profilBeanProperty() {
        return profilBean;
    }


    // Utilitie's methods:

    @Override
    public String toString() {
        return profilBean.getValue() + " : " + super.toString();
    }
}
