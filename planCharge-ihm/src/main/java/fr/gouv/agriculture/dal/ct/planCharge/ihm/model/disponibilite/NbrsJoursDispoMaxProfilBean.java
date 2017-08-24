package fr.gouv.agriculture.dal.ct.planCharge.ihm.model.disponibilite;

import fr.gouv.agriculture.dal.ct.metier.dto.AbstractDTO;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.ProfilBean;
import fr.gouv.agriculture.dal.ct.planCharge.ihm.model.referentiels.RessourceHumaineBean;
import javafx.beans.property.FloatProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

public class NbrsJoursDispoMaxProfilBean extends AbstractDisponibilitesRessourceProfilBean<AbstractDTO, NbrsJoursDispoMaxProfilBean, FloatProperty> {

    // Fields :


    // Constructors :

    public NbrsJoursDispoMaxProfilBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull ProfilBean profilBean, @NotNull Map<LocalDate, FloatProperty> calendrier) {
        super(ressourceHumaineBean, profilBean, calendrier);
    }

    public NbrsJoursDispoMaxProfilBean(@NotNull RessourceHumaineBean ressourceHumaineBean, @NotNull ProfilBean profilBean) {
        super(ressourceHumaineBean, profilBean);
    }


    // Getters/Setters :


    // Implementation of AbstractBean :


    // Utilitie's methods:

}
