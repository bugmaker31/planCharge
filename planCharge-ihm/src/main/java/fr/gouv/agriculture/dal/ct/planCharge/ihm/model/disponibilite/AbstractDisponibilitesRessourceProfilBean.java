package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public abstract class AbstractDisponibilitesRessourceProfilBean<D extends AbstractDTO, B extends AbstractDisponibilitesRessourceProfilBean<D, B, T>, T extends Property> extends AbstractDisponibilitesRessourceBean<D, B, T> {


    // Fields :

    @NotNull
    private Property<ProfilBean> profilBean = new SimpleObjectProperty<>();


    // Constructors:

    public AbstractDisponibilitesRessourceProfilBean(@NotNull RessourceHumaineBean ressourceHumaineBean,@NotNull ProfilBean profilBean, @NotNull Map<LocalDate, T> calendrier) {
        super(ressourceHumaineBean, calendrier);
        this.profilBean.setValue(profilBean);
    }

    public AbstractDisponibilitesRessourceProfilBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull ProfilBean profilBean) {
        super(ressourceHumaineBean);
        this.profilBean.setValue(profilBean);
    }


    // Getters/Setters:

    @NotNull
    public final ProfilBean getProfilBean() {
        return profilBean.getValue();
    }

    @NotNull
    public Property<ProfilBean> profilBeanProperty() {
        return profilBean;
    }

    // Methods:

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;
        if (!super.equals(o)) return false;

        AbstractDisponibilitesRessourceProfilBean that = (AbstractDisponibilitesRessourceProfilBean) o;

        return super.equals(that) && getProfilBean().equals(that.getProfilBean());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + getProfilBean().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return profilBean + " " +  super.toString();
    }
}
