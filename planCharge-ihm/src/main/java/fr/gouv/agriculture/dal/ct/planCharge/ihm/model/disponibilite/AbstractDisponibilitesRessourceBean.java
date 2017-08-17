package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public abstract class AbstractDisponibilitesRessourceBean<D extends AbstractDTO, B extends AbstractDisponibilitesRessourceBean<D, B, T>, T> extends AbstractDisponibilitesBean<D, B, T> {


    // Fields :

    @NotNull
    final RessourceHumaineBean ressourceHumaineBean;


    // Constructors:

    public AbstractDisponibilitesRessourceBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull Map<LocalDate, T> calendrier) {
        super(calendrier);
        this.ressourceHumaineBean = ressourceHumaineBean;
    }


    // Methods:

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;
        if (!super.equals(o)) return false;

        //noinspection unchecked
        AbstractDisponibilitesRessourceBean<D, B, T> that = (AbstractDisponibilitesRessourceBean<D, B, T>) o;

        return ressourceHumaineBean.equals(that.ressourceHumaineBean);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = (31 * result) + ressourceHumaineBean.hashCode();
        return result;
    }


    // Getters/Setters:

    @NotNull
    public final RessourceHumaineBean getRessourceHumaineBean() {
        return ressourceHumaineBean;
    }


    // Utilitie's methods:

    @Override
    public final String toString() {
        return ressourceHumaineBean + " : " + super.toString();
    }
}
