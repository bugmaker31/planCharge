package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import fr.gouv.agriculture.dal.ct.planCharge.util.number.PercentageProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public class PctagesDispoMaxProfilBean extends AbstractDisponibilitesRessourceBean<AbstractDTO, PctagesDispoMaxProfilBean, PercentageProperty> {

    // Fields :

    @NotNull
    private ProfilBean profilBean;


    // Constructors :

    public PctagesDispoMaxProfilBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull ProfilBean profilBean, @NotNull Map<LocalDate, PercentageProperty> calendrier) {
        super(ressourceHumaineBean, calendrier);
        this.profilBean = profilBean;
    }

    public PctagesDispoMaxProfilBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull ProfilBean profilBean) {
        super(ressourceHumaineBean);
        this.profilBean = profilBean;
    }


    // Getters/Setters :

    @NotNull
    public ProfilBean getProfilBean() {
        return profilBean;
    }


    // Implementation of AbstractBean :


    // Utilitie's methods:


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;
        if (!super.equals(o)) return false;

        PctagesDispoMaxProfilBean that = (PctagesDispoMaxProfilBean) o;

        return super.equals(that) && getProfilBean().equals(that.getProfilBean());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getProfilBean().hashCode();
        return result;
    }
}
