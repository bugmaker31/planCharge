package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public abstract class AbstractDisponibilitesRessourceProfilBean<D extends AbstractDTO, B extends AbstractDisponibilitesRessourceProfilBean<D, B, T>, T> extends AbstractDisponibilitesRessourceBean<D, B, T> {


    // Fields :

    @NotNull
    private ProfilBean profilBean;


    // Constructors:

    public AbstractDisponibilitesRessourceProfilBean(@NotNull RessourceHumaineBean ressourceHumaineBean,@NotNull ProfilBean profilBean, @NotNull Map<LocalDate, T> calendrier) {
        super(ressourceHumaineBean, calendrier);
        this.profilBean = profilBean;
    }

    public AbstractDisponibilitesRessourceProfilBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull ProfilBean profilBean) {
        super(ressourceHumaineBean);
        this.profilBean = profilBean;
    }


    // Getters/Setters:

    @NotNull
    public final ProfilBean getProfilBean() {
        return profilBean;
    }


    // Methods:

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;
        if (!super.equals(o)) return false;

        NbrsJoursDispoMaxProfilBean that = (NbrsJoursDispoMaxProfilBean) o;

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
