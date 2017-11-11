package fr.gouv.agriculture.dal.ct.planCharge.ihm.model;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public abstract class AbstractCalendrierParProfilBean<D extends AbstractDTO, B extends AbstractCalendrierParProfilBean<D, B, P>, P extends Property>
        extends AbstractCalendrierBean<D, B, P> {


    // Fields :

    @NotNull
    private Property<ProfilBean> profilBean = new SimpleObjectProperty<>();


    // Constructors:

    public AbstractCalendrierParProfilBean(@NotNull ProfilBean profilBean, @NotNull Map<LocalDate, P> calendrier) {
        super(calendrier);
        this.profilBean.setValue(profilBean);
    }

    public AbstractCalendrierParProfilBean(@NotNull ProfilBean profilBean) {
        this(profilBean, new TreeMap<>()); // TreeMap juste pour trier afin de faciliter le débogage.
    }


    // Methods:

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;
//        if (!super.equals(o)) return false;

        //noinspection unchecked
        AbstractCalendrierParProfilBean<D, B, P> that = (AbstractCalendrierParProfilBean<D, B, P>) o;

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
